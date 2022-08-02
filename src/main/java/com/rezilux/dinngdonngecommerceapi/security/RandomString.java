package com.rezilux.dinngdonngecommerceapi.security;

public class RandomString {
    // function to generate a random string of length n
    public static String getAlphaNumericString(int n, boolean numeric, boolean letter) {
        // chose a Character random from this String
        String numericString = "0123456789";
        String letterString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz";
        String alphaNumericString = "";
        // create StringBuffer size of AlphaNumericString
        if (numeric) alphaNumericString += numericString;
        if (letter) alphaNumericString += letterString;
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index = (int) (alphaNumericString.length() * Math.random());
            // add Character one by one in end of sb
            sb.append(alphaNumericString.charAt(index));
        }
        return sb.toString();
    }
}
