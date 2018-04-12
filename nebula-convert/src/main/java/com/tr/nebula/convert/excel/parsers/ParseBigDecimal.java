package com.tr.nebula.convert.excel.parsers;

import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class ParseBigDecimal extends IsParser<BigDecimal> {
    @Override
    public BigDecimal parse(Object o, Field field) {
        return isValid(o) ? new BigDecimal(o.toString()) : null;
    }

    @Override
    public void setCell(BigDecimal o, Cell cell, Field field) {
        if (o != null) {
            cell.setCellValue(o.doubleValue());
        }
    }
}
