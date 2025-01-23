package org.example;

public class InvalidTicketNumberException extends RuntimeException {
    public InvalidTicketNumberException(String message) {
        super(message);
    }
}
