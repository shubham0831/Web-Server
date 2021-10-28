package ServerPackage.HttpUtils.HtmlUtils;

public class HtmlGenerator {

    public HtmlGenerator(){

    }

    public String getBasicHTML (String text) {
        String tempHTML = "" +
                "<html>" +
                    "<head>" +
                        "<title>Test App</title>" +
                    "</head>" +
                    "<body>" +
                        "<h1>"+text+"</h1>" +
                    "</body>" +
                "</html>";
        return tempHTML;
    }

}
