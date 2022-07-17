package com.example.bookshop.web.services;

import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class CookieUtil {

    /**
     * The method removes a book from a specific Cookie
     *
     * @param cookieName  the name of the Cookie to update, for example "postponedBooks"
     * @param cookieValue current value cookie, can contain several books at once
     *                    for example "book-bqr-bsi/book-ebf-jyu/book-ekp-gdh"
     * @param slug        unique identifier for the book being removed from the Cookie
     */
    public static void removeBookFromCookie(String cookieValue, String cookieName, String slug, HttpServletResponse response) {
        ArrayList<String> cookieBooks = new ArrayList<>(Arrays.asList(cookieValue.split("/")));
        cookieBooks.remove(slug);
        Cookie cookie = new Cookie(cookieName, String.join("/", cookieBooks));
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * The method updates a specific Cookie
     * after a user tries to add a Book to this Cookie
     *
     * @param name  the name of the Cookie to update, for example "postponedBooks"
     * @param value current value cookie, can contain several books at once
     *              for example "book-bqr-bsi/book-ebf-jyu/book-ekp-gdh"
     * @param slug  unique identifier for the book being added to the Cookie
     */
    public static void updateCookie(String value, String name, String slug, HttpServletResponse response) {
        Cookie cookie = createCookie(name, value);
        if (value == null || value.equals("")) {
            cookie.setValue(slug);
        } else if (!value.contains(slug)) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(value).add(slug);
            cookie.setValue(stringJoiner.toString());
        }
        response.addCookie(cookie);
    }

    public static void clearCookieByName(HttpServletResponse response, String name) {
        Cookie cookie = createCookie(name, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public static boolean isCookieEmpty(String value) {
        return value == null || value.equals("");
    }

    public static String getValue(HttpServletRequest httpServletRequest, String name) {
        Cookie cookie = WebUtils.getCookie(httpServletRequest, name);
        return cookie != null ? cookie.getValue() : null;
    }

    private static Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setDomain("localhost");
        return cookie;
    }

}
