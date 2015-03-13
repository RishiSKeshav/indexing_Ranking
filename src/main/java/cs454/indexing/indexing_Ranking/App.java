package cs454.indexing.indexing_Ranking;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class App 
{
    public static void main( String[] args )
    {
    	String path = "I:\\books\\CS454(information Retrieval)\\data\\Crawler\\Crawler.json";

		 //String path ="I:\\books\\CS454(information Retrieval)\\data\\Crawler\\" + args[1];

		readJson(path);

    }
    
    private static void readJson(String path)
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
				JSONObject jsonObject = (JSONObject) o;
				String localpath = jsonObject.get("localpath").toString();
				
				
				
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
}
