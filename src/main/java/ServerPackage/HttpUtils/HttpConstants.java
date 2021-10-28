package ServerPackage.HttpUtils;

public class HttpConstants {

    /**USED FOR CORRECT FORMAT OF RESPONSE**/
    public static final String CRLF = "\r\n";

    /**HTTP METHODS**/
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String VERSION = "HTTP/1.0";

    /**HTTP CODES**/
    public static final String OK = "200 OK";
    public static final String BAD_REQUEST = "400 Bad Request";
    public static final String NOT_FOUND = "404 Not Found";
    public static final String NOT_ALLOWED = "405 Method Not Allowed";

    /**FOR SPECIFYING INFORMATION**/
    public static final String CONTENT_LENGTH = "Content-Length:";
    public static final String CONNECTION_CLOSE = "Connection: close";

    public static String generateHeader (String VERSION, String CODE){
        String HEADER = VERSION + " " + CODE + CRLF;
        return HEADER;
    }




}
