/*
    Author Name : Shubham Pareek
    Class function : Is where we process the command line arguments the user types in
    Project Number : 1
*/
package ServerPackage.InvertedIndex;

//this is the class to instantiate the UI and is where we will be making all our queries from
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Scanner;


public class ProjectUI {
    //will be needing a ReviewList, the reviewFile, a QAList and the qaFile
    //when user calls creates this class, that is when we actually populate our index
    private ReviewList r;
    private QAList qa;
    private String reviewFile;
    private String qaFile;

    private boolean running;

    public ProjectUI (ReviewList r, String reviewFile, QAList qa, String qaFile){
        //instantiating the variables and creating our invertedIndex
        this.r = r;
        this.qa = qa;
        this.reviewFile = reviewFile;
        this.qaFile = qaFile;
        this.running = true;
        createIndex();
    }

    private void createIndex (){
        //method to add file to the respective inverted index, and start the whole process
        r.addToIndex(reviewFile);
        qa.addToIndex(qaFile);
    }

    public ArrayList<String> getAsin (String word){
        word = word.toLowerCase();
        //method to print the items having the given asin key
        ArrayList<String> reviewAsin = r.findAsin(word);
        System.out.println("----");
        ArrayList<String> qsAsin = qa.findAsin(word);

        //we add all the results to this arraylist
        ArrayList<String> allAsin = new ArrayList<>();

        //combining both the results
        for (String asin : reviewAsin){
            allAsin.add(asin);
        }

        for (String asin : qsAsin){
            allAsin.add(asin);
        }

        return allAsin;

    }

    public ArrayList<String> getReviewSearch (String word){
        //method to print the items having the given word in the review index
        return r.searchIndex(word);
    }

    public ArrayList<String> getQaSearch (String word){
        //method to print the items having the given word in the qa index
        return qa.searchIndex(word);
    }

    public ArrayList<String> getReviewPartialSearch (String word){
        //method to print the items having the given partial word in the review index
        HashSet<String> partialSearchResult = r.partialSearchIndex(word);

        ArrayList<String> partialSearchResultList = new ArrayList<>();

        for (String result : partialSearchResult){
            partialSearchResultList.add(result);
        }

        return partialSearchResultList;
    }

    public ArrayList<String> getQaPartialSearch (String word){
        //method to print the items having the given partial word in the qa index
        HashSet<String> partialSearchResult = qa.partialSearchIndex(word);

        ArrayList<String> partialSearchResultList = new ArrayList<>();

        for (String result : partialSearchResult){
            partialSearchResultList.add(result);
        }

        return partialSearchResultList;
    }


}
