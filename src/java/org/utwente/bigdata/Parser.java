package org.utwente.bigdata;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Parser {
	
	private String inputPath;
	private DBConnection connection;
	private String dbTable = "sampleset";
	

	public Parser(String inputPath, DBConnection connection) {
		this.inputPath = inputPath;
		System.out.println("Path to input file: "+inputPath);
		this.connection = connection;
		System.out.println("Connected to database...");
		
	}
	
	private void run() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(inputPath));
			String line;
			System.out.println("Inserting data...");
			while ((line = br.readLine()) != null) {
				// Every line looks like:  wordX||wordY;digit
				if(line.matches("(.+)(\\|{2})(.+)(;{1})([0-9]+)"))  {
					
					// Get first word (wordX)
					String wordX = "";
					String[] s2 = line.split("(\\|{2})(.+)(;{1})([0-9]+)");
					for(String s : s2) {
						wordX += s;
					}
					// Now we have wordX
					
					// Get second word (wordY)
					String wordY = "";
					// First, get tuple (wordX||wordY)
					String tuple = "";
					String[] s1 = line.split("(;{1})([0-9]+)");
					for(String s : s1) {
						tuple += s;
					}
					// Now get rid of first word and the splitter (wordX||)
					String[] s4 = tuple.split("("+wordX+"{1})(\\|{2})");
					for(String s : s4) {
						wordY += s;
					}
					// Now we have wordY
					
					// Get digit
					String digit = "";
					String[] s0 = line.split("(.+)(\\|{2})(.+)(;{1})");
					for(String s : s0) {
						digit += s;
					}
					// Now we have a digit
					
					// Let's insert it into our database
					connection.insertData("INSERT INTO "+dbTable+" (word1,word2,count) VALUES ('"+wordX+"','"+wordY+"','"+digit+"')");
				} else {
					// Hier komen pairs met linebreaks uit.
					// Kan waarschijnlijk wel weggewerkt worden.
					// Voor nu even laten voor wat het is.
					//System.out.println("<"+line+">");
				}
			}
			System.out.println("Data inserted!");
			br.close();
			connection.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("Initiate database connection...");
		DBConnection connection = new DBConnection();
		System.out.println("Initiate parser...");
		Parser parser = new Parser(args[0],connection);
		parser.run();
		
		System.exit(0);		
		
	}

}
