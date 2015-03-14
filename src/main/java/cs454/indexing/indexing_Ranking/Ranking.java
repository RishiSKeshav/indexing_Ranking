package cs454.indexing.indexing_Ranking;

import java.io.FileNotFoundException;
import java.io.FileReader;
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

public class Ranking
{
	Map<String,Integer> outgoingLinksCountMap = new HashMap();
	Map<String,List<String>> outgoingLinksMap = new HashMap();
	Map<String,Integer> incomingLinksCountMap = new HashMap();
	
	public void mainFunction(String path)
	{
		readJson(path);
		
		for (Map.Entry<String, Integer> entry : incomingLinksCountMap.entrySet()) {
				System.out.println("url: " + entry.getKey());
				System.out.println("count: " + entry.getValue());
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
					
					parse(url);
					findIncomingLinks(url);
					
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
	
	private void findIncomingLinks(String url)
	{
		int count =0;
		
		if(incomingLinksCountMap.containsKey(url))
			count=incomingLinksCountMap.get(url);
	
		
		for (Map.Entry<String, List<String>> entry : outgoingLinksMap.entrySet()) {
			List<String> links = entry.getValue();
			
			if(links.contains(url))
			{				
				count++;
			}
		}
		
		if(count>0)
		{
			if(incomingLinksCountMap.containsKey(url))
				incomingLinksCountMap.put(url, incomingLinksCountMap.get(url)+1);
			else
				incomingLinksCountMap.put(url, count);
		}		
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
}