/*
 Author Name : Shubham Pareek
 Class function : Main
 Project Number : 1
*/

/*
    This file contains the main method, what this method does is given a valid input, it will create a reviewList and QAList object and then pass that object
    alongside the fileLoc to the ProjectUI object. Then it will start the whole UI.

    Validation of input is done with the help of validateParameters method.
*/

package ServerPackage.InvertedIndex;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.HashSet;

public class AmazonSearch {

    public static void main(String[] args) {
        //getting file arg
        String reviewFile = "/home/shubham/IdeaProjects/project3-shubham0831/Cell_Phones_and_Accessories_5.json";
        String qaFile = "/home/shubham/IdeaProjects/project3-shubham0831/qa_Cell_Phones_and_Accessories.json";

        ReviewList reviewList = new ReviewList("ISO-8859-1"); //creating ReviewList
        QAList qaList = new QAList("ISO-8859-1"); //creating QAList

        ProjectUI ui = new ProjectUI(reviewList, reviewFile, qaList, qaFile);
    }
}
