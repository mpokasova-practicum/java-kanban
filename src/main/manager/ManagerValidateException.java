package main.manager;

public class ManagerValidateException extends RuntimeException {
    public ManagerValidateException() {
      super();
    }
    public ManagerValidateException(String message) {
        super(message);
    }
}
