package exception;

/**
 * Created by snow.zhang on 2015/9/7.
 */
public class DBException extends Exception {

    public DBException(){
        super();
    }

    public DBException(String message){
        super(message);
    }

    public DBException(int errorCode, String message){
//        if (errorCode == )
        super(errorCode+":"+message);
    }
}
