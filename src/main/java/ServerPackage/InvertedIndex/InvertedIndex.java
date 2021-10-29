/*
 Author Name : Shubham Pareek
 Class function : Blueprint of an inverted index, and how it will store data.
 Project Number : 1
*/

//references https://stackoverflow.com/questions/11796985/java-regular-expression-to-remove-all-non-alphanumeric-characters-except-spaces to get regex for removing non-alphanumeric characters
//           https://www.geeksforgeeks.org/iterate-treemap-in-reverse-order-in-java/ to learn how to iterate a treemap in reverse order

package ServerPackage.InvertedIndex;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class InvertedIndex {
    /*
        Constructor -> doesn't take any argument

        Algorithm for populating the invertedIndex -> 1) Split document to words, then add each word to the inverted index, and the document which the word occurs in, in the hashset of documents
                                                      2) Also while we are inserting a word, we also update the count of the number of times the given word has occurred in the wordCountInDoc

        Algorithm for fullSearch -> 1) Retrieve the set of documents which have the given word
                                    2) Get count of number of times the word has appeared in the document
                                    3) Store the count and docId in a treeMap with the count being the id
                                    4) Traverse the treeMap in reverse order and keep adding the documents to the arrayList
                                    5) Return the arrayList

        Algorithm for partialSearch -> 1) For every key in inverted index, if the key contains the word, store the document in a set
                                       2) Return the set of documents. Since we do not need to implement sorting, this is enough.

    */

    //invertedIndex is a HashMap with a particular word as the key, and the corresponding value being the set of documents the given word appears in.
    private HashMap <String, HashSet<String>> invertedIndex; //is the inverted index

    //this hashmap will have docIds as the keys, and the value will be a HashMap which has the word as the key and the count of that word in the doc as the value
    private HashMap <String, HashMap<String, Integer>> wordCountInDoc; //is the hashmap we use for retrieving the count of the word in a given document

    //locks for making inverted index thread safe
    private ReentrantReadWriteLock lock;
    private Lock readLock;
    private Lock writeLock;


    public InvertedIndex (){
        //constructor just for instantiating the invertedIndex and the wordCountInDoc
        this.invertedIndex = new HashMap<>();
        this.wordCountInDoc = new HashMap<>();
        this.lock = new ReentrantReadWriteLock();
        this.readLock = lock.readLock();
        this.writeLock = lock.writeLock();
    }

    public void addDocument (String line, String docId){
        //method to add documents to the invertedIndex
        //given a sentence, this method will break down the sentence to individual words then add the word and docId to the invertedIndex

        writeLock.lock();
        try {
            String[] words = line.split(" ");
            for (String word : words) {
                word = word.toLowerCase().strip(); //every word that will enter our index and count will be in lower case

                //first we update the invertedIndex
                if (!invertedIndex.containsKey(word) && word != "") {
                    //if our invertedIndex does not have the word, we create the set which stores all the document the
                    //word is in then we enter the word-set pair into the invertedIndex hashmap
                    HashSet<String> docSet = new HashSet<>();
                    docSet.add(docId);
                    invertedIndex.put(word, docSet);
                } else if (word != "") {
                    //else get the set of docs which contain the word, and add current docs to that set
                    invertedIndex.get(word).add(docId);
                }

                //now we update the word count
            /*
                [docid, [word, count]]
             */
                if (!wordCountInDoc.containsKey(docId) && word != "") {
                    //if we do not have the document in our wordCountInDoc, we add it, along with the word and the number of times it has appeared in the doc (ie. 1 in this case)
                    HashMap<String, Integer> wordCount = new HashMap<>();
                    wordCount.put(word, 1);
                    wordCountInDoc.put(docId, wordCount);
                } else if (word != "") {
                    //if wordCountInDoc does contain docId, then check if the given doc has seen the word before or not
                    if (!wordCountInDoc.get(docId).containsKey(word)) {
                        //if this is the first instance of the word in our document, then we put the word in along with its initial count
                        //as one
                        wordCountInDoc.get(docId).put(word, 1);
                    } else {
                        //otherwise we just update the count of the word
                        int newCount = wordCountInDoc.get(docId).get(word) + 1; //updating the count
                        wordCountInDoc.get(docId).put(word, newCount); //putting the new count
                    }
                }
            }
        }finally {
            writeLock.unlock();
        }
    }


    public ArrayList<String> find (String key){
        readLock.lock();
        try {
        /*
            method to find and return the list of docs which have the given word
            method returns an arraylist of document id's, the list is sorted in descending order of the number of times the given word has appeared in it.
         */
            key = key.toLowerCase(); //all indexes are in lowerCase
            HashSet<String> docs = searchIndex(key, true); // is the set of doc ID which contains the given string //fullWord is true because we need to do a fullSearch

            if (docs == null) {
                //if our set is empty we just return an empty arraylist
                return new ArrayList<String>();
            }

            //structure of this tree map is as follows
            //key -> integer value having the count of the word
            //value -> list of documents that have the word and the count of the word is the same as the key
            TreeMap<Integer, ArrayList<String>> docWordCount = new TreeMap<>(Collections.reverseOrder()); //using tree map because it sorts the input on the basis of the key
            for (String docId : docs) {
                int count = wordCountInDoc.get(docId).get(key); //for every document that contains the word, get the count of the word
                if (!docWordCount.containsKey(count)) {
                    //using an arraylist since our docs are in a set and we do not have to worry about duplicates
                    ArrayList<String> docList = new ArrayList<>();
                    docList.add(docId);
                    docWordCount.put(count, docList);
                } else {
                    docWordCount.get(count).add(docId);
                }
            }

            //once we have the treemap, we can just iterate it (which will be in descending order) and add all the docs which have the given word in a sorted order by
            //term frequency
            ArrayList<String> docsInSortedOrder = new ArrayList<>();
            for (int count : docWordCount.keySet()) {
                //count will be in descending order, for each count we have a bunch of documents (documents with same number of occurence of the given word)
                //we put those documents in the docsInSortedOrder arraylist  then return the arrayList
                ArrayList<String> docList = docWordCount.get(count);
                for (String doc : docList) {
                    docsInSortedOrder.add(doc);
                }
            }
//        System.out.println(docsInSortedOrder.size()); //temp solution to check count of docs found
            return docsInSortedOrder;
        }finally {
            readLock.unlock();
        }
    }

    public HashSet<String> partialFind (String key){
        readLock.lock();
        try {
        /*
            Method for partialFind, takes in input a word, and return a set of all the documents which contain the word
        */
            key = key.toLowerCase(); //all indexes are in lowerCase
            HashSet<String> docs = searchIndex(key, false); // is the set of doc ID which contains the given string //fullWord is false because we need to do a partial search
//        System.out.println(docs.size()); //temp solution to check count of docs found
            return docs;
        }finally {
            readLock.unlock();
        }
    }

    private HashSet<String> searchIndex (String word, boolean fullWord){
        readLock.lock();
        try {
        /*
            This is the method where we will actually search for the word

            params -> word -> word to be searched for
                      fullWord -> is a boolean which if true means we search for a full word
                                  else we search for partial word

            This method returns a set of docId which contains the given word, that set can then be used to get access to the full doc

        */
            if (fullWord) {
                //searching for full word, just looking up at the invertedIndex
                return invertedIndex.get(word);
            }

            //searching for partial word
            //since method returns a set, we create one, and add documents which contain said partial word into this set
            HashSet<String> docs = new HashSet<>();//set of docs which contain the partial word
            for (String key : invertedIndex.keySet()) {
                //for every key in index, if key contains word, add all the docs which contain that key
                if (key.contains(word)) {
                    for (String d : invertedIndex.get(key)) {
                        docs.add(d);
                    }
                }
            }
            return docs;
        }finally {
            readLock.unlock();
        }
    }

    public HashMap<String, HashSet<String>> getInvertedIndex() {
        readLock.lock();
        try {
            //getter method for invertedIndex
            return new HashMap<String, HashSet<String>>(invertedIndex);
        }finally {
            readLock.unlock();
        }
    }

    public HashMap<String, HashMap<String, Integer>> getWordCountInDoc() {
        readLock.lock();
        try {
            //getter method of wordCountInDoc
            return wordCountInDoc;
        }finally {
            readLock.unlock();
        }
    }

    public void printIndex (){
        readLock.lock();
        try {
            for (String w : invertedIndex.keySet()) {
                System.out.println(invertedIndex.get(w));
            }
        }finally {
            readLock.unlock();
        }
    }
}
