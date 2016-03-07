/**
 * This is a simple class for formatting a data set for internal/faster use
 * Expects a raw text corpus with one document per line
 * @author arjun
 *
 */
import java.io.*;
import java.util.*;

public class DatasetFormatter {
	/**
	 * @param path path of file which is a collection of documents, 1 per line
	 * @param  M number of documents in the file referred to by path
	 * @return int [][] the matrix of documents with vocab serialized as the vocabulary
	 */
	HashSet<String> stopwords;
	HashSet<String> vocab;
	HashMap<Integer, String> map;  // int -> word
	HashMap<String, Integer> i_map; // inv word -> int map
	int [][] documents;
	
	public DatasetFormatter() throws Exception {
		stopwords = new HashSet<String>();
		// Set up stop words
		BufferedReader br = new BufferedReader(new FileReader("./stopwords.txt"));
		String line;
		while((line = br.readLine())!=null){
			stopwords.add(line);
		}
		br.close();
	}
	
	HashMap<String, Integer> getInvMap()
	{
		return i_map;
	}
	
	HashMap<Integer, String> getVocabMap()
	{
		return map;
	}
	
	int [][] getDocs()
	{
		return documents;
	}
	HashSet<String> getVocab()
	{
		return vocab;
	}
	void textCorpusFormatter(String path) throws Exception {
		vocab = new HashSet<String>();
		map = new HashMap<Integer, String>();
		i_map = new HashMap<String, Integer>();
		HashMap<String, Integer> counts = new HashMap<String, Integer>();
		
		// Do a raw count of the number of documents in this corpus
		BufferedReader br = new BufferedReader(new FileReader(path));
		String doc;
		int m = 0; // doc count
		while((doc = br.readLine())!=null){
			if(doc.length()> 1)
				m++;
		}
		br.close();
		
		// Now parse documents
		documents = new int [m][]; // dummy initalization.
		ArrayList<String []> documents_list = new ArrayList<String []>();
		br = new BufferedReader(new FileReader(path));

		while((doc = br.readLine())!=null){
			if(doc.length()>1){
				doc = doc.toLowerCase();
				String tokens [] = doc.split("[[:][ ][\"][,][;][.][?][!]]+");
				documents_list.add(tokens);
				for(String s : tokens){
					if(!vocab.contains(s)){
						counts.put(s, 1);
						vocab.add(s);
					} else{
						int count = counts.get(s);
						counts.put(s, count+1);
					}
				}
			}

		}
		System.out.println("Total number of documents in the corpus: " + documents.length);

		//Remove tokens appearing less than 5 times in the corpus and stopwords
		HashSet<String> toRemove = new HashSet<String>();
		for(String s : vocab){
			if(stopwords.contains(s) || counts.get(s)<5)
				toRemove.add(s);
		}
		for(String s : toRemove)
			vocab.remove(s);

		// Generate doc ,atrix format
		// Build vocab map
		int i = 0;
		for(String s : vocab){
			map.put(i, s);
			i_map.put(s, i);
			i++;
		}

		// Now generate documents in matrix Format
		for(int doc_index = 0; doc_index < documents_list.size(); doc_index++){
			String tokens [] = documents_list.get(doc_index);
			int word_count = 0;
			for(String s : tokens){
				if(vocab.contains(s))
					word_count++;
			}
			documents[doc_index] = new int[word_count];
			int j = 0;
			for(String s : tokens){
				if(vocab.contains(s))
					documents[doc_index][j++] = i_map.get(s);
			}
		}
		
	}
	
	public static void main(String a []) throws Exception {
		DatasetFormatter formater = new DatasetFormatter();
		System.out.println("Formating this corpus...");
		formater.textCorpusFormatter("./all.txt");
		int [][] doc = formater.getDocs();
		HashMap<String, Integer> invVocabMap = formater.getInvMap();
		HashSet<String> vocabulary = formater.getVocab();
		HashMap<Integer, String> vmap = formater.getVocabMap();
		
		
		System.out.println("Printing doc matrix and vocabulary map for testing!");
		
		System.out.println("\nDoc matrix\n");
		for(int i = 0; i< doc.length; i++){
			System.out.print("[");
			for(int j=0; j<doc[i].length-1; j++){
				System.out.print(doc[i][j] + ", ");
			}
			System.out.print(doc[i][doc[i].length-1] + "]" + "\n");
		}
		
		System.out.println("\nVocab map\n");
		for(int i=0;i<vocabulary.size(); i++)
			System.out.println(i + " = " + vmap.get(i));
		
	}
}

