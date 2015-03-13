package cs454.indexing.indexing_Ranking;

import java.util.HashMap;
import java.util.Map;

public class IndexBean {
	
	String word;
	
	Map<Integer, Integer> wordCountMap = new HashMap();

	public IndexBean()
	{
		
	}
	
	public String getWord() {
		return word;
	}
    
	public void setWord(String word) {
		this.word = word;
	}

	public Map<Integer, Integer> getWordCountMap() {
		return wordCountMap;
	}

	public void setWordCountMap(Map<Integer, Integer> wordCountMap) {
		this.wordCountMap = wordCountMap;
	}
	
	
	

}
