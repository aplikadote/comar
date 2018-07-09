/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.rworks.comar.core.service.derby;

import cl.rworks.comar.core.ComarCoreUtils;
import cl.rworks.comar.core.model.ComarCategory;
import cl.rworks.comar.core.service.ComarServiceException;
import cl.rworks.comar.core.model.ComarProduct;
import cl.rworks.comar.core.model.impl.ComarProductImpl;
import cl.rworks.rservices.JSONObjectResponse;
import cl.rworks.rservices.UUIDUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.derby.shared.common.error.DerbySQLIntegrityConstraintViolationException;
import org.json.JSONObject;

/**
 *
 * @author aplik
 */
public class InsertProduct {

    private Connection connection;

    public InsertProduct(Connection connection) {
        this.connection = connection;
    }

//    @Override
//    public JSONObjectResponse execute(JSONObject jresquest) throws RServiceException {
//        try {
//            Request request = new Request(jresquest);
//            Response response = execute(request);
//            return response.toJson();
//        } catch (ComarServiceException ex) {
//            throw new RServiceException("Error", ex);
//        }
//    }
//
//    public Response execute(Request request) throws ComarServiceException {
//        ComarProduct product = request.getProduct();
//        ComarCategory category = request.getProduct();
//        execute(product);
//        return new Response(product.getId());
//    }
    public void execute(ComarProduct product, ComarCategory category) throws ComarServiceException {
        if (product.getCode().isEmpty()) {
            throw new ComarServiceException("Codigo de producto no valido");
        }

        if (category == null) {
            throw new ComarServiceException("Producto sin categoria asignada");
        }

        byte[] id = UUIDUtils.createId();
        String sql = "INSERT INTO COMAR_PRODUCT VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            int i = 1;
            ps.setBytes(i++, id);
            ps.setString(i++, product.getCode());
            ps.setString(i++, product.getDescription());
            ps.setLong(i++, ComarCoreUtils.toDatabase(product.getBuyPrice()));
            ps.setLong(i++, ComarCoreUtils.toDatabase(product.getTax()));
            ps.setLong(i++, ComarCoreUtils.toDatabase(product.getSellPrice()));
            ps.setLong(i++, ComarCoreUtils.toDatabase(product.getStock()));
            ps.setInt(i++, product.getMetric().getId());
            ps.setBytes(i++, category.getId());
            ps.executeUpdate();

            product.setId(id);
            product.setCategoryId(category.getId());
        } catch (DerbySQLIntegrityConstraintViolationException ex) {
            throw new ComarServiceException("El codigo de producto ya existe: " + product.getCode());
        } catch (SQLException ex) {
            throw new ComarServiceException("Error", ex);
        }
    }

    public static class Request {

        private ComarProduct product;

        public Request(JSONObject jrequest) {
            this.product = ComarProductImpl.create(jrequest);
        }

        public Request(ComarProduct product) {
            this.product = product;
        }

        public ComarProduct getProduct() {
            return product;
        }

    }

    public static class Response {

        private byte[] id;

        public Response(byte[] id) {
            this.id = id;
        }

        public byte[] getId() {
            return id;
        }

        public void setId(byte[] id) {
            this.id = id;
        }

        private JSONObjectResponse toJson() {
            JSONObjectResponse jresponse = new JSONObjectResponse();
            jresponse.put("id", UUIDUtils.toString(id));
            return jresponse;
        }

    }

    public static void serve(Connection connection, ComarProduct product, ComarCategory category) throws ComarServiceException {
        new InsertProduct(connection).execute(product, category);
    }
}