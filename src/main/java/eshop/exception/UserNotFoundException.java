package eshop.exception;

import java.io.IOException;

public class UserNotFoundException extends IOException {

    public UserNotFoundException(String message) {
        super(message);
    }

}
