package cs454.indexing.indexing_Ranking;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.Link;
import org.apache.tika.sax.LinkContentHandler;
import org.apache.tika.sax.TeeContentHandler;
import org.apache.tika.sax.ToHTMLContentHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xml.sax.ContentHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Ranking
{
	List<String> LinkSet = new ArrayList<String>();
	Map<String,Integer> outgoingLinksCountMap = new HashMap();
	Map<String,List<String>> outgoingLinksMap = new HashMap();
	Map<String,Integer> incomingLinksCountMap = new HashMap();
	Map<String,List<String>> incomingLinksMap = new HashMap();
	Map<String,Double> rankMap = new HashMap();
	
	
	
	public void mainFunction(String path,String rankingFileName)
	{
		readJson(path);
		
		/*for (Map.Entry<String, Integer> entry : incomingLinksCountMap.entrySet()) {
				System.out.println("url: " + entry.getKey());
				System.out.println("count: " + entry.getValue());
		}*/
		
		//File rankJsonFile = new File("I:\\books\\CS454(information Retrieval)\\data\\Crawler\\rank.json");
		File rankJsonFile = new File("I:\\books\\CS454(information Retrieval)\\data\\Crawler\\"+rankingFileName);
		System.out.println();
		System.out.println("Ranking process started");
		
		defaultRank();
		rank();
		
		writeFile(rankJsonFile);
		
		for (Map.Entry<String, Double> entry : rankMap.entrySet()) {
				System.out.println("url: " + entry.getKey());
				System.out.println("rank: " + entry.getValue());
				System.out.println("");
		}
	}
	
	
	
	public void readJson(String path) 
	{
		JSONParser parser = new JSONParser();
		Object object;
			try
			{
				object = parser.parse(new FileReader(path));
				JSONArray jsonArray = (JSONArray) object;

				for (Object o : jsonArray)
				{
					JSONObject jsonObject = (JSONObject) o;
					String url = jsonObject.get("url").toString();					
					
					if(!LinkSet.contains(url))
					{
						parse(url);					
						findIncomingLinks(url);
						LinkSet.add(url);
					}
					//System.out.println("-------------------------------------------");
					
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		
	}
	
	private void defaultRank()
	{
		int totalLinksCount = LinkSet.size();
		double temp;
		double rank;
		
		double initialRank =1.0/totalLinksCount;
		for(String url: LinkSet)
		{
			rankMap.put(url, initialRank);
		}
	}	
		
	
	private void rank()
	{	
		double rank;
		double temp;
		for(int i=0;i<10;i++)
		{
			for(String url: LinkSet)
			{
				rank=0.0;
				if(incomingLinksMap.get(url)!=null)
				{
					
					List<String> incoming = incomingLinksMap.get(url);
					
					for(String link:incoming)
					{
						rank= rankMap.get(link);
						Double newRank = rankMap.get(link)/outgoingLinksCountMap.get(link);
						
						/*if(outgoingLinksCountMap.get(url)!=null && outgoingLinksCountMap.get(url)>0)
							temp=temp+outgoingLinksCountMap.get(url);*/
						
						rank=rank+newRank;
					}
					rankMap.put(url, rank);
				}				
									
			}
		}
	}
	
	private void findIncomingLinks(String url)
	{
		int count =0;
		List<String> incomingsLinks = new ArrayList<String>();
	
		//System.out.println("Url: " + url);
		
		if(incomingLinksCountMap.containsKey(url))
			count=incomingLinksCountMap.get(url);
	
		
		for (Map.Entry<String, List<String>> entry : outgoingLinksMap.entrySet()) {
			List<String> links = entry.getValue();			
			
			if(links.size()>0)
			{
			
			if(links.contains(url))
			{				
				incomingsLinks.add(entry.getKey());
				count++;
			}
			}
		}
		
		if(count>0)
		{
			incomingLinksMap.put(url, incomingsLinks);
			
			if(incomingLinksCountMap.containsKey(url))
				incomingLinksCountMap.put(url, incomingLinksCountMap.get(url)+1);
			else
				incomingLinksCountMap.put(url, count);
		}	
		
		/*System.out.println("count: "+ count);
		if(incomingLinksMap.get(url)!=null)
		{
		for(String s: incomingLinksMap.get(url))
		{
			System.out.println(s);
		}
		}*/
	}

	public void parse(String url1)
	{
		try
		{
			Map<String,Object> metadata = new HashMap<String, Object>();
			
			Tika tika = new Tika();
			tika.setMaxStringLength(10*1024*1024); 
			Metadata met=new Metadata();
			URL url = new URL(url1);
			
			ToHTMLContentHandler toHTMLHandler=new ToHTMLContentHandler();
			ParseContext parseContext=new ParseContext();
			LinkContentHandler linkHandler = new LinkContentHandler();
			ContentHandler textHandler = new BodyContentHandler(10*1024*1024);
			TeeContentHandler teeHandler = new TeeContentHandler(linkHandler, textHandler, toHTMLHandler);		
			
			AutoDetectParser parser=new AutoDetectParser();
		    parser.parse(url.openStream(),teeHandler,met,parseContext);
		    
		    List<Link> links = linkHandler.getLinks();
		    
		    if(links.size()!=0)
		    {
		    	
		    	List<String> lists = new ArrayList<String>();
		    	outgoingLinksCountMap.put(url1, links.size());
		    	
		    	for(Link l: links)
		    	{
		    		lists.add(l.getUri());
		    	}	
		    	
		    	outgoingLinksMap.put(url1, lists);
		    }
		    
		    
		    /*System.out.println("Url: "+url1);
		    System.out.println(links.size());
		    System.out.println("---------------------------------------------------");*/
		    
		}
		catch (  MalformedURLException e1) {		    
		    System.out.println("Malformed URl");
		  }
		
		catch (Exception e){
			System.out.println("");
		}
	}
	
	public void writeFile(File file1) {
		try
		{
			
			JSONArray jsonArrayToPrint = new JSONArray();
			
				
				for (Map.Entry<String, Double> entry : rankMap.entrySet()) 
				{
					JSONObject obj = new JSONObject();
					obj.put(entry.getKey(), entry.getValue());
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

			System.out.println("Done writing into file");
		}
		catch (IOException e)
		{

		}

	}
}