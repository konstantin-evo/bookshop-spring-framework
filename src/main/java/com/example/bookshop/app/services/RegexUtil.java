package com.example.bookshop.app.services;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

    private RegexUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static final String PHONE_PATTERN = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3} ?){2}\\d{3}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3} ?)(\\d{2} ?){2}\\d{2}$"
            + "|^(\\+7|7|8)?[\\s\\-]?\\(?[489]\\d{2}\\)?[\\s\\-]?\\d{3}[\\s\\-]?\\d{2}[\\s\\-]?\\d{2}$";

    private static final String EMAIL_PATTERN = "[A-Za-z\\d._%+-]+@[A-Za-z\\d.-]+\\.[A-Za-z]{2,4}";

    public static boolean isPhoneNumber(String payload) {
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(payload);
        return matcher.matches();
    }

    public static boolean isEmail(String payload) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(payload);
        return matcher.matches();
    }
}
