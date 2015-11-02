package MiniWattUI;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

// Parent class for all Scraping Subclasses.
// There will be Scraper subclasses for scraping Google results and general web-site content. 
public class Scraper {

	// connects to a well formed url in string format.
	public Document connectToUrl(String url) {
		System.out.println("Connecting to ... " + url + "\n");
		Document doc = null;
		try {
			doc = Jsoup.connect(url).userAgent("Mozilla")
					.ignoreHttpErrors(true).timeout(0).get();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return doc;
	}

}