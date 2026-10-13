package com.example.textutils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TextUtilsTest {

    @Test
    void reversesString() {
        assertEquals("cba", TextUtils.reverse("abc"));
    }

    @Test
    void detectsPalindrome() {
        assertTrue(TextUtils.isPalindrome("A man, a plan, a canal: Panama"));
        assertFalse(TextUtils.isPalindrome("hello"));
    }

    @Test
    void countsWords() {
        assertEquals(3, TextUtils.wordCount("  one two   three "));
        assertEquals(0, TextUtils.wordCount("   "));
    }

    @Test
    void capitalizesWords() {
        assertEquals("Hello Big World", TextUtils.capitalizeWords("hELLO big   wORLD"));
    }

    @Test
    void countsLetters() {
        var freq = TextUtils.letterFrequency("Aab!");
        assertEquals(2, freq.get('a'));
        assertEquals(1, freq.get('b'));
    }
}
