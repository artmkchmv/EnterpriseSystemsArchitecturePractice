package library.web;

// Разбор значений из HTML-форм
final class FormUtils {

    private FormUtils() {
    }

    // пустая строка превращается в null
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
