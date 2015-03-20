package cs454.indexing.indexing_Ranking;

public class WordBean1
{
	int id;
	String word;
	String fileName;
	String url;
	double tfdifValue;
	int wordCount;
	
	public WordBean1()
	{
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public double getTfdifValue() {
		return tfdifValue;
	}

	public void setTfdifValue(double tfdifValue) {
		this.tfdifValue = tfdifValue;
	}

	public int getWordCount() {
		return wordCount;
	}

	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}
	
	
}
