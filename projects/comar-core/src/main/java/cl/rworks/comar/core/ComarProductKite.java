/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.rworks.comar.core;

import io.permazen.JObject;
import io.permazen.JTransaction;
import io.permazen.annotation.JField;
import io.permazen.annotation.OnCreate;
import io.permazen.annotation.PermazenType;
import io.permazen.core.util.ObjDumper;
import java.util.NavigableSet;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 *
 * @author rgonzalez
 */
@PermazenType
public abstract class ComarProductKite implements JObject, ComarProduct {

    public static ComarProductKite create() {
        JTransaction jtx = JTransaction.getCurrent();
        return jtx.create(ComarProductKite.class);
    }

    public static ComarProductKite create(String name) {
        ComarProductKite item = create();
        item.setName(name);
        return item;
    }

    public static NavigableSet<ComarProductKite> getAll() {
        return JTransaction.getCurrent().getAll(ComarProductKite.class);
    }

    public static ComarProductKite getByCode(String code) {
        JTransaction jtx = JTransaction.getCurrent();
        NavigableSet<ComarProductKite> result = jtx.queryIndex(ComarProductKite.class, "code", String.class).asMap().get(code);
        return result != null && !result.isEmpty() ? result.first() : null;
    }

    public static Stream<ComarProductKite> search(String text) {
        if (text == null) {
            return Stream.empty();
        }

        text = text.trim();
        if (text.isEmpty()) {
            return getAll().stream();
        }

        Pattern pattern = Pattern.compile(".*" + text + ".*");
        Predicate<ComarProductKite> filterCode = e -> pattern.matcher(e.getCode()).matches();
        Predicate<ComarProductKite> filterName = e -> pattern.matcher(e.getName()).matches();
        Predicate<ComarProductKite> filter = e -> filterCode.test(e) || filterName.test(e);
        Stream<ComarProductKite> stream = getAll().stream().filter(filterCode).filter(filter);

        return stream;
    }

    @JField(indexed = true, unique = true)
    public abstract Long getId();

    public abstract void setId(Long id);

    @JField(indexed = true, unique = true)
    public abstract String getCode();

    public abstract void setCode(String code);

    @OnCreate
    void onCreate() {
        setDecimalFormat(ComarDecimalFormat.ZERO);
        setUnit(ComarUnit.UNIDAD);
    }

    public String toString() {
        return String.format("[%s, %s, %s, %s, %s, %s]", getId(), getCode(), getName(), getUnit(), getDecimalFormat(), getCategory());
    }

}