package com.example.textutils;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public final class TextUtils {

    private TextUtils() { }

    public static String reverse(String s) {
        return new StringBuilder(s).reverse().toString();
    }

    public static boolean isPalindrome(String s) {
        String cleaned = s.toLowerCase().replaceAll("[^a-z0-9]", "");
        return cleaned.equals(reverse(cleaned));
    }

    public static int wordCount(String s) {
        String trimmed = s.trim();
        return trimmed.isEmpty() ? 0 : trimmed.split("\\s+").length;
    }

    public static String capitalizeWords(String s) {
        return Arrays.stream(s.trim().split("\\s+"))
                .filter(w -> !w.isEmpty())
                .map(w -> Character.toUpperCase(w.charAt(0)) + w.substring(1).toLowerCase())
                .reduce((a, b) -> a + " " + b)
                .orElse("");
    }

    public static Map<Character, Integer> letterFrequency(String s) {
        Map<Character, Integer> freq = new TreeMap<>();
        for (char c : s.toLowerCase().toCharArray()) {
            if (Character.isLetter(c)) {
                freq.merge(c, 1, Integer::sum);
            }
        }
        return freq;
    }
}
