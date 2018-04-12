package com.tr.nebula.convert.excel.importer;

import com.tr.nebula.convert.common.OnItemHandler;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;

public class XLSXImporter<T> extends ExcelImporter<T> {
    private static Logger LOGGER = LoggerFactory.getLogger(XLSXImporter.class);

    public XLSXImporter(Class dataClass) {
        super(dataClass);
    }

    public XLSXImporter(Class dataClass, boolean hasTitleRow) {
        super(dataClass, hasTitleRow);
    }

    @Override
    public List<T> importStream(InputStream inputStream) throws Exception {
        return importStream(new XSSFWorkbook(inputStream));
    }

    @Override
    public List<T> importStream(InputStream inputStream, String charSetName) throws Exception {
        LOGGER.warn("Charset" + charSetName + " ignored, Apache poi detects encoding dynamically");
        return importStream(inputStream);
    }

    @Override
    public void importStream(InputStream inputStream, OnItemHandler handler) throws Exception {
        importStream(new XSSFWorkbook(inputStream), handler);
    }

    @Override
    public void importStream(InputStream inputStream, OnItemHandler handler, String charSetName) throws Exception {
        LOGGER.warn("Charset" + charSetName + " ignored, Apache poi detects encoding dynamically");
        importStream(new XSSFWorkbook(inputStream), handler);
    }
}
