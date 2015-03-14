package cs454.indexing.indexing_Ranking;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.apache.commons.io.FilenameUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class App
{
	static List<WordBean> wordList = new ArrayList<WordBean>();
	
	static List<String> stopWordList = new ArrayList<String>();

	public static void main(String[] args) {
		
		//File file = new File("I:\\books\\CS454(information Retrieval)\\data\\Crawler\\index.json");
		
		File file = new File("I:\\books\\CS454(information Retrieval)\\data\\Crawler\\"+ args[3]);
		
		//String path = "I:\\books\\CS454(information Retrieval)\\data\\Crawler\\Extracter1.json";

		String path ="I:\\books\\CS454(information Retrieval)\\data\\Crawler\\" +args[1];

		String stopWordPath = "I:\\books\\CS454(information Retrieval)\\data\\Crawler\\words.txt";
		stopWordList=readStopWordFile(stopWordPath);
		
		
		readJson(path);
		
		writeFile(file);
		
		//System.out.println(processWord("String.*,?!"));
		
		new Ranking().mainFunction(path,args[5]);
		

	}

	private static List<String> readStopWordFile(String stopWordPath) {
		try 
		{
			BufferedReader br = new BufferedReader(new FileReader(stopWordPath));
			String sCurrentLine;
 
			while ((sCurrentLine = br.readLine()) != null) {
				
				StringTokenizer st = new StringTokenizer(sCurrentLine);
				
				while (st.hasMoreTokens()) {
					
					String word = st.nextToken();
					stopWordList.add(word.trim().toLowerCase());					
				}
				//System.out.println(sCurrentLine);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} 
 
		return stopWordList;
	}

	private static void readJson(String path) throws NoSuchElementException
    {
		JSONParser parser = new JSONParser();
		Object object;
		
		

		try
		{
			object = parser.parse(new FileReader(path));
			JSONArray jsonArray = (JSONArray) object;

			System.out.println(jsonArray.size());
			for (Object o : jsonArray)
			{
				WordBean word = new WordBean();
				
				JSONObject jsonObject = (JSONObject) o;
				String localpath = jsonObject.get("localpath").toString();
				String fileName = FilenameUtils.getBaseName(localpath);				
				
				System.out.println(localpath);
				BodyContentHandler bodyHandler = readFile(localpath);
				
				if(bodyHandler!=null)
				{
					StringTokenizer st = new StringTokenizer(bodyHandler.toString());				
				
		        while(st.hasMoreTokens())
		        {	  
		        	//System.out.println(st.nextToken());
		        	int flag=0;
		        	String token="";
		        	token = st.nextToken();
		        	token = processWord(token);
		        	
		        	
		        	if(!stopWordList.contains(token.toLowerCase().trim())){
		        	
		        	for(WordBean w : wordList)
		        	{		        		
		        		if(w.getWord().equals(token))
		        		{
		        			word=w;
		        			flag=1;
		        			break;
		        		}
		        	}
		        	
		        	//System.out.println(st.nextToken());
		        	if(flag==0)
		        	{
		        		WordBean word1 = new WordBean();
	        			Map<String, Integer> wordCount =new HashMap();
	        			wordCount.put(fileName, 1);       			
	        			
	        			word1.setWord(token);
	        			word1.setWordCountMap(wordCount);      			
	        			
	        			wordList.add(word1);
		        	}
		        	else
		        	{	       	    
		        		WordBean temp = word;
	        			wordList.remove(word);
	        			Map<String, Integer> wordCount = temp.getWordCountMap();
	        			
	        			if(wordCount.get(fileName)==null)
	        				wordCount.put(fileName, 1);
	        			else	        			
	        				wordCount.put(fileName, wordCount.get(fileName) + 1);
	        			
	        			temp.setWordCountMap(wordCount);
	        			
	        			wordList.add(temp);	        			
		        	}
		        	
		            //System.out.println(st.nextToken());
		        }
		        }
		        }
		        System.out.println("------------------------------------------------------");
			
			
			
		}
		}

		catch (FileNotFoundException e)
		{
			System.out.println("File not found");
		}
		catch (IOException e)
		{
			
			e.printStackTrace();
		}
		catch (ParseException e)
		{
			
			e.printStackTrace();
		}
	}

	private static String processWord(String x) {
	    return x.replaceAll("[\\]\\[(){}\\*,.;!?<>%]", "");
	}
	
	private static BodyContentHandler readFile(String localpath) {
		Map<String, Object> metadata = new HashMap<String, Object>();

		File f = new File(localpath);

		BasicFileAttributes attr;
		try
		{
			Parser parser = new AutoDetectParser();

			long start = System.currentTimeMillis();
			BodyContentHandler handler = new BodyContentHandler(10000000);
			Metadata metadata1 = new Metadata();
			InputStream content = TikaInputStream.get(new File(f
					.getAbsolutePath()));
			try
			{
				parser.parse(content, handler, metadata1, new ParseContext());
			}
			catch (TikaException e)
			{

				e.printStackTrace();
			}
			catch (SAXException e)
			{
				e.printStackTrace();
			}

			attr = Files.readAttributes(Paths.get(f.getAbsolutePath()),
					BasicFileAttributes.class);

			if (metadata1.get("Content-Type").contains("html")
					|| metadata1.get("Content-Type").contains("xhtml"))
				return handler;
			else
				return null;
		}
		catch (IOException e)
		{
			System.out.println("Failed to read file ");
			return null;
		}

	}
	
	public static void writeFile(File file1) {
		try
		{
			
			JSONArray jsonArrayToPrint = new JSONArray();
			
			
			for(WordBean w: wordList)
			{
				JSONObject obj = new JSONObject();
				System.out.println("");
				System.out.println(w.getWord());
				
				for (Map.Entry<String, Integer> entry : w.getWordCountMap().entrySet()) 
				{
					obj.put(w.getWord(), w.getWordCountMap());
					System.out.println("Document: " + entry.getKey() + "Count: " + entry.getValue());
				}
				
				jsonArrayToPrint.add(obj);
			}
			
			
			
			
			/*http://examples.javacodegeeks.com/core-java/gson/gsonbuilder/enable-pretty-print-json-output-using-gson-example/
*/			
			Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
			Gson uglyJson = new Gson();
			String pretJson = prettyGson.toJson(jsonArrayToPrint);

			FileWriter file = new FileWriter(file1.getAbsolutePath(), true);
			file.write(pretJson.toString());
			file.write("\n\n");
			file.flush();
			file.close();

			System.out.println("Done");
		}
		catch (IOException e)
		{

		}

	}
	
	
}
