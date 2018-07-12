/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.rworks.comar.core.service.derby;

import cl.rworks.comar.core.TestUtils;
import cl.rworks.comar.core.service.ComarServiceException;
import java.sql.Connection;
import java.sql.SQLException;
import cl.rworks.comar.core.model.ProductoEntity;

/**
 *
 * @author aplik
 */
public class InsertProductoPorCodigoTest {

    public static void main(String[] args) {
        try (Connection conn = TestUtils.createConnection()) {
            try {
                ProductoEntity p = InsertProductoPorCodigo.serve(conn, "xxx", "Bebidas");
                System.out.println(p);
//                conn.commit();
            } catch (ComarServiceException e) {
                e.printStackTrace();
            } finally {
                conn.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
