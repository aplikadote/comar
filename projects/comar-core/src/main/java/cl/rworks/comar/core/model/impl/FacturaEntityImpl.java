/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.rworks.comar.core.model.impl;

import java.time.LocalDate;
import cl.rworks.comar.core.model.FacturaEntity;
import cl.rworks.comar.core.util.UUIDUtils;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author aplik
 */
public class FacturaEntityImpl implements FacturaEntity {

    private byte[] id;
    private String codigo;
    private LocalDate fecha;

    public byte[] getId() {
        return id;
    }

    public void setId(byte[] id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String code) {
        this.codigo = code;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate batchDate) {
        this.fecha = batchDate;
    }

    @Override
    public String toString() {
        return "FacturaEntityImpl{" + "id=" + UUIDUtils.toString(id) + ", codigo=" + codigo + ", fecha=" + fecha + '}';
    }

    public static FacturaEntity create(ResultSet rs) throws SQLException {
        byte[] id = rs.getBytes(1);
        String codigo = rs.getString(2);
        Date date = rs.getDate(3);

        FacturaEntity e = new FacturaEntityImpl();
        e.setId(id);
        e.setCodigo(codigo);
        e.setFecha(date.toLocalDate());
        return e;
    }

    public static FacturaEntity create(String codigo, LocalDate fecha) {
        FacturaEntity e = new FacturaEntityImpl();
        e.setCodigo(codigo);
        e.setFecha(fecha);
        return e;
    }
}
