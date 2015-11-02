package MiniWattUI;

import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

// getLinks() is the main function of GoogleScraper-> flow of it is : 
// 1. users passes some input as String[]
// 2. make appropriate google search url with input
// 3. connect to the search url and get links. 
// 4. repeats 2 and 3 for how many pages of results the user wants to see. 

// sample usage ...
// int pageNum = 2;
// GoogleScraper gs = new GoogleScraper(pageNum);	
// String[] input = new Scanner(System.in).nextLine().split(" ");
// for (String link : gs.getLinks(input)) System.out.println(link);

public class GoogleScraper extends Scraper{
	// GoogleScraper object could be refactored to have input member (String[]) within constructor
	// .getInput() is redundant and .getSearchUrl + .getLinks will just access this.input etc.
	
	int pageNum; // pageNum is how many pages of search results user wants. 
	public GoogleScraper(int pageNum) {
		this.pageNum = pageNum;
	}
	
	@SuppressWarnings("resource")
	public String[] getInput() {
		return new Scanner(System.in).nextLine().split(" ");
	}
	
	// User input will be made to a proper google search url, helper to getLinks();
		// multiple search paramters are seperated by %20
		// %20 is url encoding escape sequence for spaces (" ")
	public String getSearchUrl(String[] input, int page) {

		String query = input[0];
		if(input.length > 1) {
			for(int i = 1; i < input.length; i++)
				query = query + "%20" + input[i];
		}
		System.out.println("query = " + query);
			//  https://www.google.com/search?as_q=dog&start=10 (example link for second page)
		if (page > 1)
			return "https://www.google.com/search?as_q=" + query + "&start=" + (page - 1) * 10;
		else
			return "https://www.google.com/search?as_q=" + query;
	}
	
	// Returns the actual url link from each search result. 
	// Returns the actual url link from each search result. 
		public ArrayList<String> getLinks(String[] input) {
			
			ArrayList<String> links = new ArrayList<String>();

			for(int i = 1; i < pageNum + 1; i++) {
				// get the appropriate search url from user input and page num... 
				String searchUrl = getSearchUrl(input, i); 		
				// connect....
				Document doc = connectToUrl(searchUrl);
				// now parse the search results page...
				Elements results = doc.select(".r a");
				for(Element result : results) {
					
					String base = result.toString();
					//<a href="/url?q=http://www.dogparkinlafayette.org/&amp;sa=U&amp;ei=tuzFU-3-FNKryATC8ILwDQ&amp;ved=0CEIQoAIwCg&amp;usg=AFQjCNEKnqeVHggoQYKr-0yjHqx9uipPVg">Shamrock Dog Park</a>
						// cut away unnecessary stuff. 
					if(base.indexOf("http") == -1) //links important to us start with http.
						continue;
					
					String encodedUrl = base.substring(base.indexOf("http"), base.indexOf('&'));
					// the received url is encoded with the escape sequences such as :
						// http://www.w3schools.com/tags/ref_urlencode.asp
					// To actually connect to these urls, we must decode them.
					String decodedUrl = "";
					try {
						decodedUrl = java.net.URLDecoder.decode(encodedUrl, "UTF-8");
					} catch (UnsupportedEncodingException e) { e.printStackTrace();}
					links.add(decodedUrl);
				}
			}
			return links;
		}
	
	public ArrayList<String> getInfo(ArrayList<String> links, String[] input){
		ArrayList<String> info = new ArrayList<String>();
		Document doc = null;
		for(String link : getLinks(input)){
			doc = connectToUrl(link);
			Elements results = doc.select("p");

			for(Element result : results) {
				String resultString = result.toString();
				
				info.add(Jsoup.parse(resultString).text());
			}
		}
		System.out.println(info);
		return info;
	}
}