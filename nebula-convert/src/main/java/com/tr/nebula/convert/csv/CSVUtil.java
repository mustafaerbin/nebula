package com.tr.nebula.convert.csv;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tr.nebula.convert.common.Converter;
import com.tr.nebula.convert.common.annotation.Convert;
import com.tr.nebula.convert.csv.parsers.ParseDate;
import com.tr.nebula.convert.csv.parsers.Parsers;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Locale;

public class CSVUtil {

    static CellProcessor[] convertFieldsToCellProcessors(Collection<Converter.FieldEntry> fields, String[] fieldNames) {
        CellProcessor[] processors = new CellProcessor[fields.size()];
        int i = 0;
        for (Converter.FieldEntry fieldEntry : fields) {
            Field field = fieldEntry.getValue();
            Convert an = field.getAnnotation(Convert.class);

            CellProcessorAdaptor a = decideAdaptor(field);
            CellProcessor p;
            if (an.optional()) {
                if (a != null) {
                    p = new Optional(a);
                } else {
                    p = new Optional();
                }
            } else {
                if (a != null) {
                    p = new NotNull(a);
                } else {
                    p = new NotNull();
                }
            }
            fieldNames[i] = field.getName();
            processors[i++] = p;
        }
        return processors;
    }

    static CellProcessorAdaptor decideAdaptor(Field field) {
        String fieldType = field.getType().getSimpleName().toUpperCase(Locale.ENGLISH);
        // if it is enum convert name to ENUM
        if (field.getType().getGenericSuperclass() != null) {
            if (field.getType().getGenericSuperclass().toString().startsWith("java.lang.Enum")) {
                fieldType = "ENUM";
                return Parsers.valueOf(fieldType).getParser(field.getType());
            }
        }
        if (fieldType.equals("DATE")) {
            if (field.getAnnotation(JsonFormat.class) != null) {
                String format = field.getAnnotation(JsonFormat.class).pattern();
                return new ParseDate(format);
            } else {
                throw new RuntimeException("Date type must have SimpleDateFormat annotation with a valid format.");
            }
        } else {
            return Parsers.valueOf(fieldType).getParser();
        }


    }
}
