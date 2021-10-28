/*
    Author Name : Shubham Pareek
    Class function : Contains a list of the Review object as well as the mapping to and fro from docId to document
    Project Number : 1
     Reference:
        https://stackoverflow.com/a/38194881 to learn how to convert an Object ArrayList to that of a specified class
*/
package ServerPackage.InvertedIndex;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.*;
import java.util.ArrayList;

/*
    This class extends the ItemList class, and we only have to write code for the abstract methods in the ItemList class
    Maintains a list of all Review.
    This is where we actually read the review file and populate the required datastructures for retrieval later

    constructor only takes as input the charSet of the file it will have to read
*/

public class ReviewList extends ItemList {

    private String charSet; //charset of the file we will be reading

    private ArrayList<Review> reviewList; //is the list containing all the reviews

    //we create our own id's, for the case of QA the id's will be R1,R2 and so on.
    private String idPrefix = "R";
    private int idNum = 0;

    public ReviewList (String charset){
        super(); //calling on parent class constructor
        //initializing variables
        this.charSet = charset;
        reviewList = new ArrayList<>();
    }

    @Override
    public void populateItemList(String fileLoc) {
        //given a file, this method reads the file, creates the Review object and adds the object to the reviewList
        File f = new File(fileLoc);
        Gson gson = new Gson();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), charSet))) {
            String line;
            while ((line = br.readLine()) != null){
                try {
                    Review review = gson.fromJson(line, Review.class);
                    reviewList.add(review);
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
        //method to obtain the reviewText and populate the reviewsToBeIndexed list
        for (Review r : reviewList){
            String review = r.getReviewText();
            itemsToBeIndexed.add(cleanString(review));
        }
    }

    @Override
    public void addToIndex(String fileLoc) {
        //method the user will call to add the reviews file to the index
        //this method will start the whole process of having to fill out the datastructures

        populateItemList(fileLoc); //first we read and populate the itemList
        populateItemsToBeIndexed(); //then we populate the list of reviews we want to index //ie. reviewText, Questions, Answers, etc
        populateAsinToItems(); //we also populate the asinToItem HashMap

        for (String review : itemsToBeIndexed){
            //for every item in itemToBeIndexed, we create an id for the item and then add it to our index
            //we then keep a track of which id is which doc in the idToItems map
            idNum++; //our id starts from R1
            String docId = idPrefix+idNum;
            index.addDocument(review, docId);
            idToItems.put(docId, review);
        }
    }

    @Override
    public void populateAsinToItems() {
        //method to populate the AsinToItems map
        for (Review r : reviewList){
            //for every review, we get the asin and the required text and then add those to the map
            String asin = r.getAsin().toLowerCase();
            String review = r.getReviewText().toLowerCase();

            if (!asinToItem.containsKey(asin)){
                //if this is the first time we encounter the asin key, add it to the map along with the review
                ArrayList<String> reviews = new ArrayList<>();
                reviews.add(review);
                asinToItem.put(asin, reviews);
            }
            else {
                //else just add the review to map.get(asin)
                asinToItem.get(asin).add(review);
            }
        }
    }
}
