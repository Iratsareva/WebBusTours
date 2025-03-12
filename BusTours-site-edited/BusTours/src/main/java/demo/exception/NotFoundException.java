package demo.exception;

public class NotFoundException extends ClientException{
    public NotFoundException(String message) {
        super(message);
    }

}
