import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.io.*;
import java.util.*;

public class BooleanRetrieval {
	
	HashMap<String, Set<Integer>> invIndex;
	int [][] docs;
	HashSet<String> vocab;
	HashMap<Integer, String> map;  // int -> word
	HashMap<String, Integer> i_map; // inv word -> int map

	public BooleanRetrieval() throws Exception{
		// Initialize variables and Format the data using a pre-processing class and set up variables
		invIndex = new HashMap<String, Set<Integer>>();
		DatasetFormatter formater = new DatasetFormatter();
		formater.textCorpusFormatter("./all.txt");
		docs = formater.getDocs();
		vocab = formater.getVocab();
		map = formater.getVocabMap();
		i_map = formater.getInvMap();
	}

	void createPostingList(){
		//Initialze the inverted index with a SortedSet (so that the later additions become easy!)
		for(String s:vocab){
			invIndex.put(s, new TreeSet<Integer>());
		}
		//for each doc
		for(int i=0; i<docs.length; i++){
			//for each word of that doc
			for(int j=0; j<docs[i].length; j++){
				//Get the actual word in position j of doc i
				String w = map.get(docs[i][j]);
				
				/* TO-DO:
				Get the existing posting list for this word w and add the new doc in the list. 
				Keep in mind doc indices start from 1, we need to add 1 to the doc index , i
				 */
			}

		}
	}

	Set<Integer> intersection(Set<Integer> a, Set<Integer> b){
		/*
		First convert the posting lists from sorted set to something we 
		can iterate easily using an index. I choose to use ArrayList<Integer>.
		Once can also use other enumerable.
		 */
		ArrayList<Integer> PostingList_a = new ArrayList<Integer>(a);
		ArrayList<Integer> PostingList_b = new ArrayList<Integer>(b);
		TreeSet result = new TreeSet();

		//Set indices to iterate two lists. I use i, j
		int i = 0;
		int j = 0;

		while(i!=PostingList_a.size() && j!=PostingList_b.size()){
		
			//TO-DO: Implement the intersection algorithm here

		}
		return result;
	}

	Set <Integer> evaluateANDQuery(String a, String b){
		return intersection(invIndex.get(a), invIndex.get(b));
	}

	Set<Integer> union(Set<Integer> a, Set<Integer> b){
		/*
		 * This is very simple if we use Java Collections and its methods
		 * TO-DO: Figure out how to perform union?
		 */
		TreeSet result = new TreeSet();
		// Implement Union here
		return result;
	}

	Set <Integer> evaluateORQuery(String a, String b){
		return union(invIndex.get(a), invIndex.get(b));
	}
	
	Set<Integer> not(Set<Integer> a){
		TreeSet result = new TreeSet();
		/*
		 Hint:
		 NOT is very simple. I traverse the sorted posting list between i and i+1 index
		 and add the other (NOT) terms in this posting list between these two pointers
		 First convert the posting lists from sorted set to something we 
		 can iterate easily using an index. I choose to use ArrayList<Integer>.
		 Once can also use other enumerable.
		 
		 
		 */
		
		ArrayList<Integer> PostingList_a = new ArrayList<Integer>(a);
		int total_docs = docs.length;
		// TO-DO: Implement the not method using above idea or anything you find better!
		return result;
	}

	Set <Integer> evaluateNOTQuery(String a){
		return not(invIndex.get(a));
	}
	
	Set <Integer> evaluateAND_NOTQuery(String a, String b){
		return intersection(invIndex.get(a), not(invIndex.get(b)));
	}
	public static void main(String[] args) throws Exception {

		//Initialize parameters
		BooleanRetrieval model = new BooleanRetrieval();

		//Generate posting lists
		model.createPostingList();

		//Print the posting lists from the inverted index
		
		System.out.println("\nPrinting posting list:");
		for(String s : model.invIndex.keySet()){
			System.out.println(s + " -> " + model.invIndex.get(s));	
		}
		

		//Print test cases

		System.out.println();
		
		System.out.println("\nTesting AND queries \n");
		System.out.println("1) " + model.evaluateANDQuery("mouse", "keyboard"));
		System.out.println("2) " + model.evaluateANDQuery("mouse", "wifi"));
		System.out.println("3) " + model.evaluateANDQuery("button", "keyboard"));
				
		System.out.println("\nTesting OR queries \n");
		System.out.println("4) " + model.evaluateORQuery("wifi", "scroll"));
		System.out.println("5) " + model.evaluateORQuery("youtube", "reported"));
		System.out.println("6) " + model.evaluateORQuery("errors", "report"));
		
		System.out.println("\nTesting AND_NOT queries \n");
		System.out.println("7) " + model.evaluateAND_NOTQuery("mouse", "scroll"));
		System.out.println("8) " + model.evaluateAND_NOTQuery("scroll", "mouse"));
		System.out.println("9) " + model.evaluateAND_NOTQuery("lenovo", "logitech"));

	}

}