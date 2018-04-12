package com.tr.nebula.convert.xml.parsers;

import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;
import java.lang.reflect.Field;

public class ParseDouble extends IsParser<Double> {
    @Override
    public Double parse(JsonParser parser, Field field) throws IOException {
        return isValid(parser) ? new Double(parser.getValueAsDouble()) : null;
    }
}
