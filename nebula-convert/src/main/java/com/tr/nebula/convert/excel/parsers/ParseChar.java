package com.tr.nebula.convert.excel.parsers;

import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;

public class ParseChar extends IsParser<Character> {
    @Override
    public Character parse(Object o, Field field) {
        return isValid(o) ? o.toString().charAt(0) : null;
    }

    @Override
    public void setCell(Character o, Cell cell, Field field) {
        if (o != null) {
            cell.setCellValue(o.toString());
        }
    }
}
