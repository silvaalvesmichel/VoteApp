package br.com.vote.service.util;


public class Util {

    public static boolean isnull(String value) {
        if (value == null || value.equals("")) {
            return true;
        }
        return false;
    }

}
