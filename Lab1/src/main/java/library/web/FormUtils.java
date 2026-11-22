package library.web;

final class FormUtils {

    private FormUtils() {
    }

    static String trim(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    static Integer toInt(String value) {
        String s = trim(value);
        return s == null ? null : Integer.valueOf(s);
    }
}
