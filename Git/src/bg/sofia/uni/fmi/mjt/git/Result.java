package bg.sofia.uni.fmi.mjt.git;

public class Result {
    private boolean isSuccessful;
    private String message;

    public Result(boolean isSuccessful, String message) {
        this.isSuccessful = isSuccessful;
        this.message = message;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public String getMessage() {
        return message;
    }

}
