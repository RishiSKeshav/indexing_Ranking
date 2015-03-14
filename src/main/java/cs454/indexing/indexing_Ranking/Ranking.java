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

public class Ranking {
	Map<String, Integer> outgoingLinksCountMap = new HashMap();
	Map<String, List<String>> outgoingLinksMap = new HashMap();
	Map<String, Integer> incomingLinksCountMap = new HashMap();

	public void mainFunction(String path) {
		readJson(path);
	}

	public void readJson(String path) {
		JSONParser parser = new JSONParser();
		Object object;
		try {
			object = parser.parse(new FileReader(path));
			JSONArray jsonArray = (JSONArray) object;

			for (Object o : jsonArray) {
				JSONObject jsonObject = (JSONObject) o;
				String url = jsonObject.get("url").toString();
				System.out.println(url);
				
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {

			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
}