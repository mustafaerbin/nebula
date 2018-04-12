package com.tr.nebula.convert.xml.parsers;

import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;
import java.lang.reflect.Field;

import static java.lang.Enum.valueOf;

public class ParseEnum extends IsParser<Enum> {
    @Override
    public Enum parse(JsonParser parser, Field field) throws IOException {
        Class<? extends Enum> enumClass = (Class<? extends Enum>) field.getType();
        return isValid(parser) ? valueOf(enumClass, parser.getValueAsString()) : null;
    }
}
