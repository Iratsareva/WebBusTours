package demo.exception;

public class PasNotFoundException extends RuntimeException{

    public PasNotFoundException(String message) {
        super(message);
    }
}

