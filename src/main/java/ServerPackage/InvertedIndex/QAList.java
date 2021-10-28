/*
 Author Name : Shubham Pareek
 Class function : Contains a list of the QuestionAnswer object as well as the mapping to and fro from docId to document
 Project Number : 1
*/

package ServerPackage.InvertedIndex;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.*;
import java.util.ArrayList;


/*
    This class extends the ItemList class, and we only have to write code for the abstract methods in the ItemList class
    Maintains a list of all QuestionAnswer.
    This is where we actually read the question answer file and populate the required datastructures for retrieval later

    constructor only takes as input the charSet of the file it will have to read
*/

public class QAList extends ItemList{

    String charSet; //charset of the file we will be reading

    ArrayList<QuestionAnswer> qaList; //is the list containing all the qa

    //we create our own id's, for the case of QA the id's will be Q1,Q2 and so on.
    String idPrefix = "Q";
    int idNum = 0;

    public QAList (String charSet){
        super(); //calling on parent class constructor
        //initializing variables
        this.charSet = charSet;
        this.qaList = new ArrayList<>();
    }


    @Override
    public void populateItemList(String fileLoc) {
        //given a file, this method reads the file, creates the QuestionAnswer object and adds the object to the qaList
        File f = new File(fileLoc);
        Gson gson = new Gson();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), charSet))) {
            String line;
            while ((line = br.readLine()) != null){
                try {
                    QuestionAnswer qa = gson.fromJson(line, QuestionAnswer.class);
                    qaList.add(qa);
                }
                catch (JsonSyntaxException jse){
                    //everytime we have a JsonSyntaxException we just continue on.
                    continue;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void populateItemsToBeIndexed() {
        //method to extract questions and answers and populate the itemsToBeIndexed list
        for (QuestionAnswer qa : qaList){
            String questionAnswer = qa.getQuestion() + " " + qa.getAnswer();
            itemsToBeIndexed.add(cleanString(questionAnswer));
        }
    }

    @Override
    public void addToIndex(String fileLoc) {
        //method the user will call to add the reviews file to the index
        //this method will start the whole process of having to fill out the datastructures

        populateItemList(fileLoc); //first we read and populate the itemList
        populateItemsToBeIndexed(); //then we populate the list of items we want to index //ie. reviewText, Questions, Answers, etc
        populateAsinToItems(); //we also populate the asinToItems HashMap over here

        for (String qa : itemsToBeIndexed){
            //for every item in itemToBeIndexed, we create an id for the item and then add it to our index
            //we then keep a track of which id is which doc in the idToItems map
            idNum++; //our id starts from Q1
            String docId = idPrefix+idNum; //creating id
            index.addDocument(qa, docId);
            idToItems.put(docId, qa);
        }
    }

    @Override
    public void populateAsinToItems() {
        //method to populate the AsinToItems map
        for (QuestionAnswer qa : qaList){
            //for every question answer, we get the asin and the required text and then add those to the map
            String asin = qa.getAsin().toLowerCase();
            String questionAnswer = qa.getQuestion() + " " + qa.getAnswer();
            questionAnswer = questionAnswer.toLowerCase();

            if (!asinToItem.containsKey(asin)){
                //if this is the first time we encounter the asin key, add it to the map along with the review
                ArrayList<String> questionAnswers = new ArrayList<>();
                questionAnswers.add(questionAnswer);
                asinToItem.put(asin, questionAnswers);
            }
            else {
                //else just add the questionAnswer to map.get(asin)
                asinToItem.get(asin).add(questionAnswer);
            }
        }
    }



}
