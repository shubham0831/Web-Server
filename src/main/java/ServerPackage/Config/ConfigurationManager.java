package ServerPackage.Config;

/**
 * https://www.baeldung.com/gson-json-to-map to learn how to use gson to parse json to a map
 */

import ServerPackage.InvertedIndex.Review;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.util.HashMap;

public class ConfigurationManager {
    private String filePath;
    private HashMap<String, String> configuration;

    public ConfigurationManager (String filePath){
        this.filePath = filePath;
        getConfig(this.filePath);
    }

    private void getConfig (String filePath){
        Gson gson = new Gson();
        File file = new File(filePath);
        /**Since our config file is small, we can just keep concatenating all the lines in the json file, then use Gson to parse them**/
        String fullJson = "";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String line;
            while ((line = br.readLine()) != null){
                fullJson += line;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        configuration = gson.fromJson(fullJson, HashMap.class);
    }

    public String getSlackToken (){
        return configuration.get("slack token");
    }

    public int getSlackBotPort (){
        String portString = configuration.get("slack-bot port");
        int port = Integer.parseInt(portString);
        return port;
    }

    public int getIndexPort (){
        String portString = configuration.get("index port");
        int port = Integer.parseInt(portString);
        return port;
    }


}
