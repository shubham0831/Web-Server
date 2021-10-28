package ServerPackage.HttpUtils.HtmlUtils;

import ServerPackage.HttpUtils.HttpConstants;

public class HtmlGenerator {

    public HtmlGenerator(){

    }

    public String getBasicHTML (String text) {
        String tempHTML =
                "<html>" +
                    "<head>" +
                        "<title>Test App</title>" +
                    "</head>" +
                    "<body>" +
                        "<h1>" + text + "</h1>" +
                    "</body>" +
                "</html>";
        return tempHTML;
    }

    public String getInputForm (String title, String action, String textBoxLabel) {
        String HEAD = generateHead(title);
        String FORM = generateForm(action, textBoxLabel);
        String BODY = generateBody(FORM);
        String HTML = HEAD + BODY;
        return HTML;
    }

    private String generateHead (String title){
        String head =
                "<html> <head> <title>" + title + "</title></head>";
        return head;
    }

    private String generateBody (String html){
        String body =
                "<body>" + html + "</body> </html>";
        return body;
    }

    private String generateForm (String action, String textBoxLabel){
        String form =
                "<form action = " + action + " method = " + HttpConstants.POST+">"+
                        "<label for = \"msg\">" + textBoxLabel + "</label> </br></br>"+
                        "<input type = \"text\" id = \"msg\" name = \"msg\"/>" +
                        "<input type = \"submit\" value = \"submit\"/>";
        return form;
    }

}
