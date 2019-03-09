package nl.klokhet.app.utils;

public class TextViewUtil {

    public static String notNullNotEmpty(String data) {
        if (data != null && !data.trim().isEmpty()) {
            return data;
        }
        return "";
    }

    public static String notNullNotEmpty(int data) {
        if (data > 0) {
            return String.valueOf(data);
        }
        return "";
    }
}
