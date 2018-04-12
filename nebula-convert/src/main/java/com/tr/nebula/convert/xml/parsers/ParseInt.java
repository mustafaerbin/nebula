package com.tr.nebula.convert.xml.parsers;

import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;
import java.lang.reflect.Field;

public class ParseInt extends IsParser<Integer> {
    @Override
    public Integer parse(JsonParser parser, Field field) throws IOException {
        return isValid(parser) ? new Integer(parser.getValueAsInt()) : null;
    }
}
