package validation;

public class FormValidation {

    private static final String REGEX_EMAIL = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
    private static final String MASCARA = "(xx) xxxxx-xxxx";

    public boolean isEmpty (String value){
        return value.trim().length() == 0;
    }

    public boolean isEmailInvalid (String value){
        return !value.matches(REGEX_EMAIL);
    }

    public boolean isBelowMinLength (String value, int length){
        return value.trim().length() < length;
    }

}
