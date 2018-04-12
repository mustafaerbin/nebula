package com.tr.nebula.common.converter;

import com.tr.nebula.common.lang.ValueEnum;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by kamilbukum on 21/03/2017.
 */
public class ValueConverter {
    /**
     *
     */
    private static Map<String, SimpleDateFormat> formatMap = new LinkedHashMap<>();

    /**
     * @param value
     * @return
     */
    public static String toString(Boolean value) {
        return value + "";
    }

    /**
     * @param value
     * @return
     */
    public static String toString(Integer value) {
        return value + "";
    }

    /**
     * @param value
     * @return
     */
    public static String toString(Long value) {
        return value + "";
    }

    /**
     * @param value
     * @return
     */
    public static String toString(Double value) {
        return value.toString() + "";
    }

    /**
     * @param value
     * @return
     */
    public static String toString(BigDecimal value) {
        return value + "";
    }

    /**
     * @param en
     * @return
     */
    public static String toString(ValueEnum<String> en) {
        return en.getValue();
    }

    /**
     * @param date
     * @return
     */
    public static String toString(Date date) {
        return date.getTime() + "";
    }


    /**
     * @param date
     * @param format
     * @return
     */
    public static String toString(Date date, SimpleDateFormat format) {
        return format.format(date);
    }

    /**
     * @param date
     * @param formatString
     * @return
     */
    public static String toString(Date date, String formatString) {
        SimpleDateFormat format = formatMap.computeIfAbsent(formatString, k -> new SimpleDateFormat(formatString));
        return format.format(date);
    }

    /**
     * @param value
     * @return
     */
    public static Integer toInteger(Boolean value) {
        return value ? 1 : 0;
    }

    /**
     * @param value
     * @return
     */
    public static Integer toInteger(String value) {
        return new Integer(value);
    }

    /**
     * @param value
     * @return
     */
    public static Integer toInteger(Long value) {
        return value.intValue();
    }

    /**
     * @param value
     * @return
     */
    public static Integer toInteger(Double value) {
        return value.intValue();
    }


    /**
     * @param value
     * @return
     */
    public static Integer toInteger(BigDecimal value) {
        return value.intValue();
    }

    /**
     * @param en
     * @return
     */
    public static Integer toInteger(ValueEnum<Integer> en) {
        return en.getValue();
    }

    /**
     * @param value
     * @return
     */
    public static Long toLong(Boolean value) {
        return value ? 1L : 0L;
    }

    /**
     * @param value
     * @return
     */
    public static Long toLong(String value) {
        return new Long(value);
    }

    /**
     * @param value
     * @return
     */
    public static Long toLong(Double value) {
        return value.longValue();
    }

    /**
     * @param value
     * @return
     */
    public static Long toLong(Integer value) {
        return new Long(value);
    }


    /**
     * @param value
     * @return
     */
    public static Long toLong(BigDecimal value) {
        return value.longValue();
    }


    /**
     * @param en
     * @return
     */
    public static Long toLong(ValueEnum<Long> en) {
        return en.getValue();
    }

    /**
     * @param value
     * @return
     */
    public static Double toDouble(String value) {
        return new Double(value);
    }

    /**
     * @param value
     * @return
     */
    public static Double toDouble(Boolean value) {
        return value ? new Double(1) : new Double(0);
    }

    /**
     * @param value
     * @return
     */
    public static Double toDouble(Integer value) {
        return new Double(value);
    }

    /**
     * @param value
     * @return
     */
    public static Double toDouble(Long value) {
        return new Double(value);
    }

    /**
     * @param value
     * @return
     */
    public static Double toDouble(BigDecimal value) {
        return value.doubleValue();
    }

    /**
     * @param en
     * @return
     */
    public static Double toDouble(ValueEnum<Double> en) {
        return en.getValue();
    }

    /**
     * @param value
     * @return
     */
    public static BigDecimal toBigDecimal(Boolean value) {
        return value ? BigDecimal.ONE : BigDecimal.ZERO;
    }

    /**
     * @param value
     * @return
     */
    public static BigDecimal toBigDecimal(String value) {
        return new BigDecimal(toDouble(value));
    }

    /**
     * @param value
     * @return
     */
    public static BigDecimal toBigDecimal(Integer value) {
        return new BigDecimal(value);
    }

    /**
     * @param value
     * @return
     */
    public static BigDecimal toBigDecimal(Long value) {
        return new BigDecimal(value);
    }

    /**
     * @param value
     * @return
     */
    public static BigDecimal toBigDecimal(Double value) {
        return new BigDecimal(value);
    }


    /**
     * @param en
     * @return
     */
    public static BigDecimal toBigDecimal(ValueEnum<BigDecimal> en) {
        return en.getValue();
    }


    /**
     *
     * @param dClass
     * @param value
     * @return
     */
    public static Object convert(Class<?> dClass, Object value) {
        try {
            if(value instanceof ValueEnum) {
                value =  ((ValueEnum) value).getValue();
                if(dClass.equals(value.getClass())) return value;
            } else if(value instanceof Enum) {
                value = ((Enum) value).name();
                if(dClass.equals(value.getClass())) return value;
            }
            Method method = ValueConverter.class.getMethod("to" + dClass.getSimpleName(), value.getClass());
            return method.invoke(dClass, value);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param clazz
     * @return
     */
    public static Class<?> getClass(Class<?> clazz) {
        switch (clazz.getName()) {
            case "boolean":
                return Boolean.class;
            case "int":
                return Integer.class;
            case "long":
                return Long.class;
            case "double":
                return Double.class;
            case "short":
                return Short.class;
            case "char":
                return Character.class;
        }
        return clazz;
    }
}
