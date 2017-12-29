/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.rworks.comar.core.impl;

import cl.rworks.comar.core.model.ComarContext;
import cl.rworks.comar.core.model.ComarProperties;
import cl.rworks.comar.core.impl.ComarPropertiesImpl;
import cl.rworks.comar.core.impl.ComarDatabaseServiceImpl;
import org.slf4j.LoggerFactory;
import cl.rworks.comar.core.service.ComarDatabaseService;

/**
 *
 * @author rgonzalez
 */
public class ComarContextImpl implements ComarContext {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ComarContextImpl.class);
    private static ComarContextImpl instance;
    //
    private ComarProperties properties;
    private ComarDatabaseService service;

    public static ComarContextImpl getInstance() {
        instance = instance == null ? new ComarContextImpl() : instance;
        return instance;
    }

    private ComarContextImpl() {
        properties = new ComarPropertiesImpl();
        service = new ComarDatabaseServiceImpl("storage");
        init();
    }

    private void init() {
    }

    @Override
    public ComarProperties getProperties() {
        return properties;
    }
    
    @Override
    public ComarDatabaseService getService(){
        return service;
    }

}
