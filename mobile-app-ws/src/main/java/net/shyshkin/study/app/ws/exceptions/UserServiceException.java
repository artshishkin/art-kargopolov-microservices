package net.shyshkin.study.app.ws.exceptions;

public class UserServiceException extends RuntimeException{

    static final long serialVersionUID = 8567507594176863989L;

    public UserServiceException(String message) {
        super(message);
    }
}
