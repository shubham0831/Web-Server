package ServerPackage.HttpUtils;

import ServerPackage.HtmlUtils.HtmlGenerator;

import java.util.ArrayList;

public class ResponseGenerator {
    public ResponseGenerator() {}

    public String generateGETResponse (String title, String action, String textBoxLabel){
        HtmlGenerator htmlGenerator = new HtmlGenerator();
        String generatedHtml = htmlGenerator.getInputForm(title, action, textBoxLabel);
        String response = getResponse(generatedHtml, HttpConstants.OK);

        return response;
    }

    public String generateInvertedIndexResponse (String title, String action, String textBoxLabel, ArrayList<String> searchResults){
        HtmlGenerator htmlGenerator = new HtmlGenerator();
        String generatedHtml = htmlGenerator.generateOutputList(title, action, textBoxLabel, searchResults);
        String response = getResponse(generatedHtml, HttpConstants.OK);
        return response;
    }

    public String generateNoItemFoundResponse(String title, String action, String textBoxLabel, String text) {
        HtmlGenerator htmlGenerator = new HtmlGenerator();
        String generatedHtml = htmlGenerator.generateNoItemsFoundHtml(title, action, textBoxLabel, text);
        String response = getResponse(generatedHtml, HttpConstants.OK);
        return response;
    }

    public String generateHomePageResponse (String text){
        HtmlGenerator htmlGenerator = new HtmlGenerator();
        String generatedHtml = htmlGenerator.getBasicHTML(text);
        String response = getResponse(generatedHtml, HttpConstants.OK);

        return response;
    }

    public String generateNOTFOUNDResponse (){
        HtmlGenerator htmlGenerator = new HtmlGenerator();
        String generatedHtml = htmlGenerator.getBasicHTML("Error 404 Page Not Found");
        String response = getResponse(generatedHtml, HttpConstants.NOT_FOUND);

        return response;
    }

    public String generateBADREQUESTResponse (){
        HtmlGenerator htmlGenerator = new HtmlGenerator();
        String generatedHtml = htmlGenerator.getBasicHTML("Error 400 Bad Request");
        String response = getResponse(generatedHtml, HttpConstants.BAD_REQUEST);

        return response;
    }

    public String generateMETHODNOTALLOWEDResponse (){
        HtmlGenerator htmlGenerator = new HtmlGenerator();
        String generateHtml = htmlGenerator.getBasicHTML("Error 405 Method Not Allowed");
        String response = getResponse(generateHtml, HttpConstants.NOT_ALLOWED);

        return response;
    }

    private String getResponse (String generatedHtml, String STATUS){
        String CRLF = HttpConstants.CRLF;
        String HEADER = HttpConstants.generateHeader(HttpConstants.VERSION, STATUS);
        String CONTENT_LENGTH = HttpConstants.CONTENT_LENGTH;

        String response =
                HEADER + //this is the status line of the response
                        CONTENT_LENGTH +generatedHtml.getBytes().length + CRLF + //the header, currently we only have the length
                        CRLF +
                        generatedHtml + //actual html content
                        CRLF + CRLF;

        return response;
    }
}
