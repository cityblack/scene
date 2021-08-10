package com.lzh.game.scene.core.exception;

public class FutureResponseException extends RuntimeException {

    public FutureResponseException(String message) {
        super(message);
    }

    public FutureResponseException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
