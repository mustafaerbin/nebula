package com.tr.nebula.convert.csv;

import com.tr.nebula.convert.common.Importer;
import com.tr.nebula.convert.common.OnItemHandler;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class CSVImporter<T> extends Importer<T> {

    private CsvPreference preference = null;
    private Collection<FieldEntry> fields = null;
    private String[] fieldNames = null;
    private CellProcessor[] processors = null;

    public CSVImporter(Class dataClass) {
        this(dataClass, CsvPreference.STANDARD_PREFERENCE);
    }

    public CSVImporter(Class dataClass, CsvPreference preference) {
        super(dataClass);
        this.preference = preference;
        this.fields = getFields(getDataClass());
        this.fieldNames = new String[fields.size()];
        this.processors = CSVUtil.convertFieldsToCellProcessors(this.fields, this.fieldNames);
    }

    @Override
    public List<T> importStream(InputStream inputStream) throws Exception {
        return importStream(inputStream, DEFAULT_ENCODING);
    }

    @Override
    public List<T> importStream(InputStream inputStream, String charSetName) throws Exception {

        final List<T> list = new LinkedList<>();

        DefaultOnItemHandler handler = new DefaultOnItemHandler(list);
        try {
            this.importStream(inputStream, handler, charSetName);
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    @Override
    public void importStream(InputStream inputStream, OnItemHandler handler) throws Exception {
        this.importStream(inputStream, handler, DEFAULT_ENCODING);
    }

    @Override
    public void importStream(InputStream inputStream, OnItemHandler handler, String charSetName) throws Exception {

        Reader reader = new InputStreamReader(inputStream, charSetName);

        ICsvBeanReader csvBeanReader = new CsvBeanReader(reader, this.preference);
        Object obj;
        while ((obj = csvBeanReader.<T>read(getDataClass(), this.fieldNames, this.processors)) != null) {
            handler.onItem(obj);
        }

    }

}
