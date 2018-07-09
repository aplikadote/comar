/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.rworks.comar.core.service.permazen;

import cl.rworks.comar.core.model.ComarProduct;
import java.util.List;
import io.permazen.JTransaction;
import io.permazen.Permazen;
import io.permazen.ValidationMode;
import java.util.ArrayList;
import cl.rworks.comar.core.service.ComarServiceException;
import cl.rworks.comar.core.service.ComarTransaction;
import cl.rworks.comar.core.model.ComarCategory;
import cl.rworks.comar.core.model.ComarMetric;
import cl.rworks.comar.core.model.ComarMetricObject;
import java.math.BigDecimal;
import cl.rworks.comar.core.service.ComarService;

/**
 *
 * @author aplik
 */
public class ComarServicePermazen implements ComarService {

    private Permazen dataSource;

    private Class[] modelClasses = new Class[]{
        ComarProductPermazen.class,
        ComarSellPermazen.class
    };

    public ComarServicePermazen() {
    }

    @Override
    public void startup(int dbOption) {
        switch (dbOption) {
            case MEMORY:
                dataSource = ComarServicePermazenUtils.createPermazen(ComarServicePermazenUtils.createOnMemoryDatabase(), modelClasses);
                break;
            case DISK:
                dataSource = ComarServicePermazenUtils.createPermazen(ComarServicePermazenUtils.createDerbyDatabase("storage"), modelClasses);
                break;
            case SERVER:
//                dataSource = KiteUtils.createPermazen(KiteUtils.createMysqlDatabase(null), modelClasses);
//                break;
                throw new RuntimeException("opcion no soportada aun");
            default:
                throw new RuntimeException("opcion no soportada: " + dataSource);
        }
    }

    @Override
    public ComarTransaction createTransaction() throws ComarServiceException {
        JTransaction jtx = dataSource.createTransaction(true, ValidationMode.AUTOMATIC);
        JTransaction.setCurrent(jtx);
        return new ComarTransactionPermazen(jtx);
    }

    @Override
    public List<ComarProduct> searchProductByCodeOrDescription(String str) {
        List<ComarProduct> rows = new ArrayList<>();
        if (str.isEmpty()) {
            ComarProductPermazen.getAll().stream().forEach((ComarProductPermazen p) -> {
                rows.add((ComarProduct) p.copyOut());
            });
        } else {
            ComarProductPermazen.search(str).stream().forEach((ComarProductPermazen p) -> {
                rows.add((ComarProduct) p.copyOut());
            });
        }
        return rows;
    }

    @Override
    public ComarProduct getProductByCode(String code) throws ComarServiceException {
        return ComarProductPermazen.getByCode(code);
    }

    @Override
    public void insertProduct(ComarProduct product, ComarCategory category) {
        String code = product.getCode();
        String description = product.getDescription();
        ComarMetric metric = product.getMetric();
        BigDecimal buyPrice = product.getBuyPrice();
        BigDecimal sellPrice = product.getSellPrice();
        BigDecimal stock = product.getStock();
        BigDecimal tax = product.getTax();

        ComarProduct dbProduct = ComarProductPermazen.create();
        dbProduct.setCode(code);
        dbProduct.setDescription(description);
        dbProduct.setMetric(metric);
        dbProduct.setBuyPrice(buyPrice);
        dbProduct.setSellPrice(sellPrice);
        dbProduct.setStock(stock);
        dbProduct.setTax(tax);
    }

    @Override
    public void updateProduct(ComarProduct product, ComarCategory category) throws ComarServiceException {
//        ComarProduct dbProduct = ComarProductPermazen.getByCode(oldCode);
        String oldCode = "";
        ComarProduct dbProduct = null;
        if (dbProduct == null) {
            throw new ComarServiceException("Codigo del producto no encontrado: " + oldCode);
        }
    }

    public boolean validateCode(String code) throws ComarServiceException {
        try {
            return ComarProductPermazen.getByCode(code) == null;
        } catch (Exception ex) {
            throw new ComarServiceException("Error", ex);
        }
    }

    @Override
    public List<ComarCategory> getAllCategories() throws ComarServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void insertCategory(ComarCategory category) throws ComarServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ComarProduct> getProductForCategory(ComarCategory category) throws ComarServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ComarProduct> getAllProducts() throws ComarServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteCategory(ComarCategory category) throws ComarServiceException{
         throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void deleteProducts(List<ComarProduct> products) throws ComarServiceException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void editProductCode(ComarProduct model, String code) throws ComarServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void editProductDescription(ComarProduct model, String description) throws ComarServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void editProductMetric(ComarProduct model, ComarMetric metric) throws ComarServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void updateCategoryOfProducts(List<ComarProduct> products, ComarCategory model) throws ComarServiceException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ComarMetricObject> getAllMetrics() throws ComarServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}