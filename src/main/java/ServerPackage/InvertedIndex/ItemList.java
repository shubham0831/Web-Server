/*
    Author : Shubham Pareek
    Class Purpose : Abstract class containing common methods for both ReviewList and QAList Class
    Project : 1
    https://stackoverflow.com/a/64271783 to learn how to match a whole word
*/
package ServerPackage.InvertedIndex;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Pattern;

/*
    ReviewList and QAList have a lot of the same methods. So we make an abstract class ItemList which will have those common methods as well as abstract methods for when we
    need our own implementation.

    This class contains the common methods which both the ReviewList and QAList class will need as well as abstract methods where the implementation will differ class to class.

    common methods -> cleanString, searchIndex, partialSearchIndex, validateQuery, findASIN, printKey, printPartialKey, printAsin (Essentially all retrieval will be the same)
    abstract methods -> populateList, populateListToBeIndexed, populateAsinToItems, addToIndex (All populating of the datastructures for retrieval will be different)

    common variables -> itemToBeIndexed, idToItems, asinToItem, invertedIndex
*/

public abstract class ItemList {
    //we use protected variables over here, since we need to access them and make changes to them
    protected ArrayList<String> itemsToBeIndexed; //these are the list of items in string format to be indexed
    protected HashMap<String, String> idToItems; // map containing mapping from docId to the Item
    protected HashMap<String, ArrayList<String>> asinToItem; ////map containing mapping from ASIN to a list of items having that ASIN
    protected InvertedIndex index;

    public ItemList (){
        //constructor initializes the variables
        this.itemsToBeIndexed = new ArrayList<>();
        this.idToItems = new HashMap<>();
        this.asinToItem = new HashMap<>();
        this.index = new InvertedIndex();
    }

    //abstract methods start
    public abstract void populateItemList (String fileLoc); //method to populate the ReviewList or QAList
    public abstract void populateItemsToBeIndexed (); //method to get the string we want to index, will be different for ReviewList and QAList
    public abstract void addToIndex (String fileLoc); //given a fileLoc, this method opens the file, parses the json object and essentially starts the whole process
    public abstract void populateAsinToItems (); //method to populate the asinToItem HashMap, for retrieval of asin keys
    //abstract methods end

    public String cleanString(String line){
        //given a string, method will remove all alphaNumerics from the string and return the cleaned string
        String cleanLine = line.replaceAll("[^a-zA-Z0-9\\s]", "");
        cleanLine = cleanLine.toLowerCase();
        /*
            regex explanation
                   the ^ is inside the bracket meaning that replace anything around alphanumeric charcaters with ""
                   \\s is for space, since we dont want to remove space right now
        */
        return cleanLine;
    }

    public ArrayList<String> searchIndex (String word){
        //given a word, this method will do a search and return words that match the word fully in the inverted index
        //method returns a sorted arraylist of document id's, sorting is in descending order of count of word
        boolean validQuery = validateQuery(word);
        if (!validQuery){
            //if query contains alphanumerics then it is not a valid query
            throw new InvalidParameterException("Key should contain only alphanumerics");
        }
        return index.find(word); //not checking for null and returning empty arrayList if doc doesn't contain word since inverted index will do that
    }

    public HashSet<String> partialSearchIndex (String word){
        //given a word, this method will do a search and return words that match the word partially in the inverted index
        //method returns a HashSet of documents which contain the partial word
        boolean validQuery = validateQuery(word);
        if (!validQuery){
            //if query contains alphanumerics then it is not a valid query
            throw new InvalidParameterException("Key should contain only alphanumerics");
        }
        return index.partialFind(word); //not checking for null and returning empty arrayList if doc doesn't contain word since inverted index will do that
    }

    public ArrayList<String> findAsin (String asin){
        //given an asin key, this method will sarch the asinToItem HashMap and return the result which is an ArrayList
        boolean validQuery = validateQuery(asin);
        if (!validQuery){
            //if query contains alphanumerics then it is not a valid query
            throw new InvalidParameterException("Key should contain only alphanumerics");
        }
        asin = asin.toLowerCase();
        ArrayList<String> items = asinToItem.get(asin);
        if (items == null){
            //if no elements are found then we return an empty arraylist
            System.out.println("No such item found");
            return new ArrayList<String>();
        }
//        System.out.println(items.size());
        return items;
    }

    private boolean validateQuery (String word){
        //method to validate whether the query is valid or not (ie. has only alphanumerics or not)
        // regex explained ^ -> beginning of string
        // [a-zA-Z0-9] any char which is alphanumeric
        //*$ end of string
        // ^ is outside the square brackets, meaning consider the whole string and not just where the alphanumeric characters start
        Pattern p = Pattern.compile("^[a-zA-Z0-9]*$");

        //method return true if query is valid, else false
        return p.matcher(word).find();
    }

    public void printKey (String word){
        //method to call when we want to print all the reviews that have given word
        //returns nothing
        ArrayList<String>  docList = searchIndex(word);

        if (docList.size() == 0){
            System.out.println("No such element found");
            return;
        }

        for (String id : docList){
            System.out.println(idToItems.get(id));
            System.out.println("-----------");
        }

        return;
    }

    public void printPartialKey (String word){
        //method to call when we want to print all reviews that contain the given partial word
        //returns nothing
        HashSet<String> docList = partialSearchIndex(word);

        if (docList.size() == 0){
            System.out.println("No such element found");
            return;
        }

        for (String id : docList){
            System.out.println(idToItems.get(id));
            System.out.println("-----------");
        }

        return;
    }

    public void printAsin (String asin){
        //method to call when we want to print all the Items which have the given ASIN number
        //returns nothing
        ArrayList<String> docs = findAsin(asin);

        if (docs.size() == 0){
            System.out.println("No such element found");
            return;
        }

        for (String review : docs){
            System.out.println(review);
            System.out.println("-----------");
        }
    }

    public HashMap<String, String> getIdToItems() {
        //returns mapping from the docId to the items
        return idToItems;
    }

    public InvertedIndex getIndex (){
        //returns the inverted index
        return index;
    }

    public HashMap<String, ArrayList<String>> getAsinToItem() {
        //returns the mapping from the ASIN key to the DocId
        return asinToItem;
    }
}
