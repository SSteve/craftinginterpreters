package com.craftinginterpreters.lox;

public class RuntimeError extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 2284854715226445885L;
    final Token token;

    RuntimeError(Token token, String message) {
        super(message);
        this.token = token;
    }
}
