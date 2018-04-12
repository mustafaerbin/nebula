package com.tr.nebula.convert.excel.parsers;

import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;

public class ParseByte extends IsParser<Byte> {
    @Override
    public Byte parse(Object o, Field field) {
        return isValid(o) ? Byte.valueOf(o.toString()) : null;
    }

    @Override
    public void setCell(Byte o, Cell cell, Field field) {
        if (o != null) {
            cell.setCellValue(o);
        }
    }
}
