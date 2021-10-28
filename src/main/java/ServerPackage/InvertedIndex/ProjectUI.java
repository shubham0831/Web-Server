/*
    Author Name : Shubham Pareek
    Class function : Is where we process the command line arguments the user types in
    Project Number : 1
*/
package ServerPackage.InvertedIndex;

//this is the class to instantiate the UI and is where we will be making all our queries from
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

    public void startUI (){
        //method to start our UI, and get commands
        while (running){
            getCommand();
        }
    }

    private void getCommand (){
        //method to take as input the user command and figure out what the user wants to do
        System.out.println("Enter command, anything else will exit the code");

        Scanner sc = new Scanner(System.in, "ISO-8859-1");
        String command = sc.nextLine();
        if (command.split(" ").length != 2){
            //our commands have to be of 2 words
            if (command.equals("exit")){
                running = false;
                return;
            }
//            throw new IllegalArgumentException("Invalid command, try again");
            System.out.println("Invalid command, quitting program");
            running = false;
            return;
        }
        String instruction = command.split(" ")[0].strip(); //getting the instruction
        String word = command.split(" ")[1].strip(); //getting the word

        //if-else to figure out what command is for what and run appropriate method
        if (instruction.equals("find")){
            printAsin(word);
            System.out.println("==========================================");
        }
        else if (instruction.equals("reviewsearch")){
            reviewSearch(word);
            System.out.println("==========================================");
        }
        else if (instruction.equals("qasearch")){
            qaSearch(word);
            System.out.println("==========================================");
        }
        else if (instruction.equals("reviewpartialsearch")){
            reviewPartialSearch(word);
            System.out.println("==========================================");
        }
        else if (instruction.equals("qapartialsearch")){
            qaPartialSearch(word);
            System.out.println("==========================================");
        }
        else {
            //if command is anything else, we stop running our program
            running = false;
        }
        //end of if-else
        return;
    }


    private void printAsin (String word){
        //method to print the items having the given asin key
//        System.out.println("reviews : ");
//        r.printAsin(word.toLowerCase());
        r.findAsin(word);
//        System.out.println("QAs : ");
//        qa.printAsin(word.toLowerCase());
        qa.findAsin(word);
    }

    private void reviewSearch (String word){
        //method to print the items having the given word in the review index
        r.printKey(word);
//        r.searchIndex(word);
    }

    private void qaSearch (String word){
        //method to print the items having the given word in the qa index
        qa.printKey(word);
//        qa.searchIndex(word);
    }

    private void reviewPartialSearch (String word){
        //method to print the items having the given partial word in the review index
        r.printPartialKey(word);
//        r.partialSearchIndex(word);
    }

    private void qaPartialSearch (String word){
        //method to print the items having the given partial word in the qa index
        qa.printPartialKey(word);
//        qa.partialSearchIndex(word);
    }


}
