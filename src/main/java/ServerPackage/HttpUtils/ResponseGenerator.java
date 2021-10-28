package ServerPackage.HttpUtils;

import ServerPackage.HttpUtils.HtmlUtils.HtmlGenerator;

public class ResponseGenerator {
    public ResponseGenerator() {}

    public String generateResponse (String text){
        HtmlGenerator htmlGenerator = new HtmlGenerator();
        String generatedHtml = htmlGenerator.getBasicHTML(text);

        String CRLF = "\n\r";
        String response =
                "HTTP/1.1 200 OK" + CRLF + //this is the status line of the response
                        "Content-Length: " +generatedHtml.getBytes().length + CRLF + //the header, currently we only have the length
                        CRLF +
                        generatedHtml + //actual html content
                        CRLF + CRLF;

        return response;
    }
}
