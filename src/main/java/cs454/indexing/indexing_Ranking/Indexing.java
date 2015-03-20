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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;

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

public class Indexing
{
	static Map<String,List<WordBean1>> wordMap = new HashMap<String, List<WordBean1>>();
	static List<WordBean1> wordList = new ArrayList<WordBean1>();
	static List<String> stopWordList = new ArrayList<String>();

	public static void main(String[] args) throws IOException 
	{
		      	debug();
		      
				/*File temp = new File(args[1]);
				
				File file = new File(temp.getParent()+"\\"+args[5]);
				
				//String path = "I:\\books\\CS454(information Retrieval)\\data\\Crawler\\Extracter1.json";

				String path =args[1];

				String stopWordPath = args[3];
				
				stopWordList=readStopWordFile(stopWordPath);
				
				System.out.println("Indexing process started");
				readJson(path);
				
				fileWriter(file);
				
				//System.out.println(processWord("String.*,?!"));
				
				new Ranking().mainFunction(path,temp.getParent()+"\\"+args[7]);*/
	}
	
	private static void debug() throws IOException
	{
		 File file = new File("C:\\Users\\RishiSuresh\\workspace\\webCrawler\\target\\data\\Crawler\\index11.json");
		 String path = "C:\\Users\\RishiSuresh\\workspace\\webCrawler\\target\\data\\Crawler\\Extracter.json";
		 String stopWordPath = "C:\\Users\\RishiSuresh\\workspace\\webCrawler\\target\\data\\Crawler\\words.txt";
			
			stopWordList=readStopWordFile(stopWordPath);
			
			System.out.println("Indexing process started");
			readJson(path);
		 
		 //writeFile(file);
			
			fileWriter(file);
		
	}
	
	@SuppressWarnings("unchecked")
	public static void fileWriter(File file1) throws IOException
	{
		JSONArray jsonArray;
		List<WordBean1> words;
		JSONObject obj;
		JSONObject jsonObjectToPrint = new JSONObject();
		
		for(String word : wordMap.keySet())
		{
			jsonArray = new JSONArray();
			words =  wordMap.get(word);
			double totalSize = words.size();
			double termFreq = 0.0;
			for(WordBean1 w : words)
			{
				obj = new JSONObject();
				obj.put("fileName", w.getFileName().toString());
				obj.put("url", w.getUrl());
				obj.put("Count", Integer.toString(w.getWordCount()));
				termFreq = w.getWordCount() / totalSize;
				termFreq =Double.parseDouble(new DecimalFormat("##.##").format(termFreq));
				obj.put("tfid", Double.toString(termFreq));
				jsonArray.add(obj);
			}
			jsonObjectToPrint.put(word, jsonArray);
			
		}
		
		FileWriter file = new FileWriter(file1.getAbsolutePath(), true);
        try {
        	
        	Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();			
			String pretJson = prettyGson.toJson(jsonObjectToPrint);
        	
            file.write(pretJson);
            System.out.println("Done Writing into file");
            
        } catch (IOException e) {
            e.printStackTrace();
 
        } finally {
            file.flush();
            file.close();
        }
		
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
			
			br.close();
 
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
			
			/*for (Object o : jsonArray)
			{
				
				JSONObject jsonObject = (JSONObject) o;
				
				JSONObject jsonObject1 =(JSONObject) jsonObject.get("metadata");
				JSONObject jsonObject2 =(JSONObject) jsonObject1.get("metadata");
				String innerMetadata="";
				
				Set<String> keys = jsonObject2.keySet();
				
				if(keys.contains("Description"))
				{
					innerMetadata = jsonObject2.get("Description").toString() ;
					System.out.println("description "+processWord(innerMetadata));
				}
				
				if(keys.contains("description"))
				{
					innerMetadata = jsonObject2.get("description").toString() ;
					System.out.println("description "+processWord(innerMetadata));
				}
				
				if(keys.contains("Author"))
				{
					System.out.println("Author "+ processWord(jsonObject2.get("Author").toString()));
					
				}
				
				if(keys.contains("title"))
				{
					System.out.println("title "+ processWord(jsonObject2.get("title").toString()));
					
				}
				
				System.out.println("-----------------------------------------------");
				
			}
			*/
			
			
			for (Object o : jsonArray)
			{
				
				JSONObject jsonObject = (JSONObject) o;
				String localpath = jsonObject.get("localpath").toString();
				String url =  jsonObject.get("url").toString();
				String metadata = jsonObject.get("metadata").toString();
				
				JSONObject jsonObject1 =(JSONObject) jsonObject.get("metadata");
				JSONObject jsonObject2 =(JSONObject) jsonObject1.get("metadata");
				
				
				Set<String> keys = jsonObject2.keySet();
				
				if(keys.contains("Description"))
				{
					String innerMetadata = jsonObject2.get("Description").toString() ;
					StringTokenizer stringTokenizer = new StringTokenizer(
							innerMetadata.toString().replaceAll("\\s+", " ")," .,-");
					
					while (stringTokenizer.hasMoreTokens()) {
						String element = stringTokenizer.nextToken();
						element = processWord(element);

						if (!stopWordList.equals((element)) && checkElements(element) == false && element.length()>2 && isNumeric(element) == false) {
							doIndexingProcess(localpath,url,element);
						}
					}
				}
				
				if(keys.contains("description"))
				{
					String innerMetadata = jsonObject2.get("description").toString() ;
					StringTokenizer stringTokenizer = new StringTokenizer(
							innerMetadata.toString().replaceAll("\\s+", " ")," .,-");
					
					while (stringTokenizer.hasMoreTokens()) {
						String element = stringTokenizer.nextToken();
						element = processWord(element);

						if (!stopWordList.equals((element)) && checkElements(element) == false && element.length()>2 && isNumeric(element) == false) {
							doIndexingProcess(localpath,url,element);
						}
					}
				}
				
				if(keys.contains("Author"))
				{
					String innerMetadata=processWord(jsonObject2.get("Author").toString());
					//System.out.println("Author "+ processWord(jsonObject2.get("Author").toString()));
					
					System.out.println(processAuthor(innerMetadata));
					
					if (!stopWordList.equals((innerMetadata)) && checkElements(innerMetadata) == false && innerMetadata.length()>2 && isNumeric(innerMetadata) == false) {
						doIndexingProcess(localpath,url,innerMetadata);
										
					}
				}
				
				if(keys.contains("title"))
				{
					//System.out.println("title "+ processWord(jsonObject2.get("title").toString()));
					String innerMetadata = jsonObject2.get("title").toString() ;
					StringTokenizer stringTokenizer = new StringTokenizer(
							innerMetadata.toString().replaceAll("\\s+", " ")," .,-");
					
					while (stringTokenizer.hasMoreTokens()) {
						String element = stringTokenizer.nextToken();
						element = processWord(element);

						if (!stopWordList.equals((element)) && checkElements(element) == false && element.length()>2 && isNumeric(element) == false) {
							doIndexingProcess(localpath,url,element);
						}
					}
				}
				
							
				System.out.println("Currently processing file "+localpath);
				BodyContentHandler bodyHandler = readFile(localpath);
				
				if(bodyHandler!=null)
				{
					StringTokenizer stringTokenizer = new StringTokenizer(
							bodyHandler.toString().replaceAll("\\s+", " ")," .,-");
					
					while (stringTokenizer.hasMoreTokens()) {
						String element = stringTokenizer.nextToken();
						element = processWord(element);

						if (!stopWordList.equals((element)) && checkElements(element) == false && element.length()>2 && isNumeric(element) == false) {
							doIndexingProcess(localpath,url,element);
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
	
	private static void doIndexingProcess(String localpath, String url,String token) {
		
		
		//System.out.println(bodyHandler.toString());
		
        		        		
        	if(wordMap.get(token)==null)
        	{
        		List<WordBean1> words = new ArrayList<WordBean1>();
        		WordBean1 word1 = new WordBean1();   			    			       			
    			
    			word1.setWord(token);
    			word1.setWordCount(1); 
    			word1.setFileName(localpath);
    			word1.setUrl(url);
    			
    			words.add(word1);
    			wordList.add(word1);
    			wordMap.put(word1.getWord(), words);
        	}
        	else
        	{
        		List<WordBean1> words = wordMap.get(token);
        		int flag=0;
        		for(WordBean1 w : words)
            	{		        		
            		if(w.getWord().equals(token) && w.getFileName().equals(localpath))
            		{
            			w.setWordCount(w.getWordCount()+1);
            			flag=1;
            			break;
            		}
            	}
        		
        		if(flag==0)
        		{
        			WordBean1 word1 = new WordBean1();   			    			       			
        			
        			word1.setWord(token);
        			word1.setWordCount(1); 
        			word1.setFileName(localpath);
        			word1.setUrl(url);
        			
        			words.add(word1);
        		}
        		wordMap.put(token, words);        		
        	}
	}
	
	private static String processAuthor(String x) {
	    return x.replace("\"", "");
	}
	
	private static String processWord(String x) {
	    return x.replaceAll("[\\]\\[(){}\\*:,.;!?<>%]", "");
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

			return handler;
			
			/*if (metadata1.get("Content-Type").contains("html")
					|| metadata1.get("Content-Type").contains("xhtml"))
				return handler;
			else
				return null;*/
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
			JSONObject obj ;
			List<WordBean1> words = new ArrayList<WordBean1>();
			
			
				for (Map.Entry<String, List<WordBean1>> entry : wordMap.entrySet()) 
				{
					obj = new JSONObject();
					
					JSONArray js = new JSONArray();;
					
					words = entry.getValue();
					
					for(WordBean1 w: words)
					{
						
						JSONObject obj1 = new JSONObject();
						
						obj1.put("URL", w.getUrl());
						obj1.put("fileName", w.getFileName());
						obj1.put("wordCount", w.getWordCount());
						obj1.put("tfid",Double.parseDouble(new DecimalFormat("##.##").format(w.getWordCount()/words.size())));
						
						js.add(obj1);
						obj.put(entry.getKey(), js);
						jsonArrayToPrint.add(obj);
					}
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

			System.out.println("Done writing into file");
		}
		catch (IOException e)
		{

		}

	}
	
	public static boolean checkElements(String element)
	{
		if(element.equals("#") || element.equals("&") || element.equals("+") || element.equals("-") || element.equals("") || element.equals("|") || element.equals(".") || element.equals("\\\\"))
			return true;
		
		return false;
	}
	
	public static boolean isNumeric(String element)
	{
		String regex = "[0-9]+";
		if(element.matches(regex))
			return true;
		
		return false;
	}
}
