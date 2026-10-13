package com.example.textutils;

public class Main {
    public static void main(String[] args) {
        String text = args.length > 0 ? String.join(" ", args) : "A man a plan a canal Panama";

        System.out.println("Input:          " + text);
        System.out.println("Reversed:       " + TextUtils.reverse(text));
        System.out.println("Palindrome?     " + TextUtils.isPalindrome(text));
        System.out.println("Word count:     " + TextUtils.wordCount(text));
        System.out.println("Capitalized:    " + TextUtils.capitalizeWords(text));
        System.out.println("Letter counts:  " + TextUtils.letterFrequency(text));
    }
}
