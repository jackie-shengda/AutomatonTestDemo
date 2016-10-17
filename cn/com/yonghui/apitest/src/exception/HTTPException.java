package exception;

/**
 * Created by snow.zhang on 2015/9/14.
 */
public class HTTPException extends Exception {

    public HTTPException(){
        super();
    }

    public HTTPException(String message){
        super(message);
    }

    public HTTPException(int errorCode, String message){
        super(errorCode + ": " +message);
    }
}
