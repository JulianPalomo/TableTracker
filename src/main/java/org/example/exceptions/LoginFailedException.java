package org.example.exceptions;


public class LoginFailedException extends Throwable {
    public LoginFailedException(String message) {
        super(message);
    }
}
