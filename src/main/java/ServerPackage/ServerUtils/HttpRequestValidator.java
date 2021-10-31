package ServerPackage.ServerUtils;

import ServerPackage.ServerUtils.Validators.HeaderValidator;
import ServerPackage.ServerUtils.Validators.RequestValidator;

import java.util.HashMap;

public class HttpRequestValidator {
    
    public static boolean validateRequest (String request){
        RequestValidator requestValidator = new RequestValidator(request);
        boolean valid = requestValidator.isValid();
        return valid;
    }
    
    public static boolean validateHeader (String header){
        HeaderValidator headerValidator = new HeaderValidator(header);
        boolean valid = headerValidator.isValid();
        return valid;
    }
    
    public static boolean validateBody (HashMap<String, String> headers, String requestType, String body){
        return true;
    }
}
