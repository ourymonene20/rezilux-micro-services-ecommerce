package com.rezilux.dinngdonngecommerceapi.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";

    // Regex for acceptable year
    public static final String NUMERIC_REGEX = "^[0-9]+$";
    public static final String CREDIT_CARD_REGEX = "^[0-9 ]+$";
    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ADMIN_ACCOUNT = "admin";
    public static final String DEFAULT_LANGUAGE = "fr";
    public static final String ANONYMOUS_USER = "anonymoususer";

    private Constants() {
    }
}
