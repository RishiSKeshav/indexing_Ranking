package cs454.indexing.indexing_Ranking;

import java.util.HashMap;
import java.util.Map;

public class WordBean {
	
	String word;
	
	Map<String, Integer> wordCountMap = new HashMap();

	public WordBean()
	{
		
	}
	
	public String getWord() {
		return word;
	}
    
	public void setWord(String word) {
		this.word = word;
	}

	public Map<String, Integer> getWordCountMap() {
		return wordCountMap;
	}

	public void setWordCountMap(Map<String, Integer> wordCountMap) {
		this.wordCountMap = wordCountMap;
	}
	
	
	

}
