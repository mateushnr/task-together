package validation;

public class FormValidation {

    private static final String REGEX_EMAIL = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";

    public boolean isEmpty (String value){
        return value.trim().length() == 0;
    }

    public boolean isEmailValid (String value){
        return value.matches(REGEX_EMAIL);
    }
}
