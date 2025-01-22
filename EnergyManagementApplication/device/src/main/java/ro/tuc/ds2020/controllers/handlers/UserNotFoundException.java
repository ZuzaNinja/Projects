package ro.tuc.ds2020.controllers.handlers;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
