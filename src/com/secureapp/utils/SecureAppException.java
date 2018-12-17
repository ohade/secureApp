package com.secureapp.utils;


/**
 * Used to throw a unified exception in the app
 */
public class SecureAppException extends RuntimeException {
    public SecureAppException(String msg){
        super(msg);
    }

    public SecureAppException(Throwable t) {
        super(t);
    }
}
