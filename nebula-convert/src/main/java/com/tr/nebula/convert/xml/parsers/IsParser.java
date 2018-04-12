package com.tr.nebula.convert.xml.parsers;

import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;
import java.lang.reflect.Field;


public abstract class IsParser<T> {

    abstract public T parse(JsonParser parser, Field field) throws IOException;

    protected static boolean isValid(JsonParser o) throws IOException {
        return o.getValueAsString() != null && !o.getValueAsString().trim().isEmpty();
    }
}
