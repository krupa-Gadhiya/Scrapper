package wikiLinks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.io.BufferedWriter;
import java.io.File;  
import java.io.IOException;  
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.FileWriter;   

public class scrappingLinks {

	static void scrapWikilinks(String wikilink, ArrayList<String> ar) throws IOException 
	{
		URL url = new URL(wikilink);
		HttpURLConnection httpURLConnect=(HttpURLConnection)url.openConnection();
	    httpURLConnect.setConnectTimeout(5000);
	    httpURLConnect.connect();
	    if(httpURLConnect.getResponseCode()>=400)
	    {
	        	System.out.println(wikilink+" - "+httpURLConnect.getResponseMessage()+" - is a broken link\nPlease enter valid link.");
	        	System.exit(0);
	    }
	    
	    System.setProperty("webdriver.gecko.driver", "C:\\SeleniumJars\\geckodriver.exe");
	    FirefoxDriver driver= new FirefoxDriver(); 
		driver.get(wikilink);
		List<WebElement> allLink=driver.findElements(By.tagName("a"));
		for(WebElement list: allLink)
		{
				ar.add(list.getAttribute("href"));
		}
		ar.removeAll(Collections.singleton(null));     
	}
	
	public static void main(String[] args) throws IOException 
	{
		@SuppressWarnings("resource")
		Scanner myObj = new Scanner(System.in);  
	    System.out.println("Enter Wikipedia link: ");
	    String wikilink = myObj.nextLine();
	    ArrayList<String> ar = new ArrayList<String>();  		    
		System.out.println("Enter number between 1 and 20: ");
		int n = myObj.nextInt();
		if(n < 1 || n > 20)
		{
		    	System.out.println("Please enter number between 1 and 20 only.");
		    	System.exit(0);
		}
		else
		   	scrapWikilinks(wikilink, ar);
		
		int count = 0;
		for(int j = 0; j < ar.size(); j++)
		{
			if(count == n)
			{
				break;
			}
		    else
		    {
		    	if(ar.get(j).contains("File:") || ar.get(j).contains("#"))
				{
				    continue;
				}
				else
				{
				    scrapWikilinks(ar.get(j), ar);
				    count++;
				}
		    }
		}
		    
		File f = new File("D:\\ScrappedLinks.csv");
		if(!(f.exists() && !f.isDirectory())) 
		{ 
				f.createNewFile();
		}
		FileWriter w = new FileWriter(f);
		BufferedWriter out = new BufferedWriter(w);
		for(int i = 0 ; i< ar.size(); i++)
		{
			out.write(ar.get(i));
			out.newLine();
		}
		out.close();
		
		HashSet<String> hset = new HashSet<String>(ar);
		System.out.println("Scrapping Done.");
		System.out.println("Total link count:");
		System.out.println(ar.size());
		System.out.println("Total unique link count:");
		System.out.println(hset.size());
	}
}