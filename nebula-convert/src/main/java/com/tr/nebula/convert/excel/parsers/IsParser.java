package com.tr.nebula.convert.excel.parsers;

import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;

public abstract class IsParser<T> {
    abstract public T parse(Object o, Field field);

    abstract public void setCell(T o, Cell cell, Field field);

    protected static boolean isValid(Object o) {
        return o != null && !o.toString().trim().isEmpty();
    }
}
