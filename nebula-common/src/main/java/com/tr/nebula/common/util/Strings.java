package com.tr.nebula.common.util;

/**
 * Provides frequently needed methods for string operations.
 */
public final class Strings {

    private Strings() {
    }

    /**
     * Capitalizes the first character of the word given.
     * It will convert i to I
     *
     * @param word
     * @return result
     */
    public static final String capitalizeFirstChar(String word) {
        return Character.toUpperCase(word.charAt(0)) + word.substring(1);
    }

    /**
     * Un capitalizes the first character of the word given.
     * It will convert i to I
     *
     * @param word
     * @return result
     */
    public static final String unCapitalizeFirstChar(String word) {
        return Character.toLowerCase(word.charAt(0)) + word.substring(1);
    }

    public static boolean has(String value) {
        return value != null && !value.trim().equals("");
    }
}
