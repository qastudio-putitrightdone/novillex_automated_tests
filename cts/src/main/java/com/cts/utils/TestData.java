package com.cts.utils;

import java.security.SecureRandom;
import java.time.LocalDate;

public class TestData {

    private static final SecureRandom RANDOM = new SecureRandom();
    public static final String ALPHA_LOWER = "abcdefghijklmnopqrstuvwxyz";
    public static final String ALPHA_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String DIGITS = "0123456789";
    public static final String ALPHANUMERIC = ALPHA_LOWER + ALPHA_UPPER + DIGITS;
    public static final String URL_SAFE = ALPHA_LOWER + ALPHA_UPPER + DIGITS + "-_";

    public static String IndiaMobileNumberGenerator() {
        final char[] START_DIGITS = {'6', '7', '8', '9'};
        StringBuilder sb = new StringBuilder(10);
        sb.append(START_DIGITS[RANDOM.nextInt(START_DIGITS.length)]);
        for (int i = 1; i < 10; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }

    private static String generate(int length, String allowedChars) {
        if (length <= 0) throw new IllegalArgumentException("length must be > 0");
        if (allowedChars == null || allowedChars.isEmpty()) throw new IllegalArgumentException("allowedChars must be non-empty");

        StringBuilder sb = new StringBuilder(length);
        int bound = allowedChars.length();
        for (int i = 0; i < length; i++) {
            int idx = RANDOM.nextInt(bound);
            sb.append(allowedChars.charAt(idx));
        }
        return sb.toString();
    }

    public static String generateRandomValue(int length) {
        return generate(length, ALPHA_LOWER);
    }

    public static String generateRandomNumber(int length) {
        return generate(length, DIGITS);
    }

    public int getTodaysDay() {
        return LocalDate.now().getDayOfMonth();
    }

    public static String generateRandomEmail() {
        return generateRandomValue(8) + "@" + generateRandomValue(5) + ".com";
    }

    public static String firstDayOfMonth(int monthsBack) {
        LocalDate now = LocalDate.now();
        LocalDate firstOfTarget = now.withDayOfMonth(1).minusMonths(monthsBack);
        return firstOfTarget.toString(); // yyyy-MM-dd
    }

    public static String firstDayOfMonth() {
        return firstDayOfMonth(0);
    }

    public static boolean isReallyBlank(String str) {
        if (str == null) return true;
        return str.replace("\u00A0", "")
                .trim()
                .isEmpty();
    }
}
