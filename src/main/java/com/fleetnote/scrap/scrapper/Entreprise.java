package com.fleetnote.scrap.scrapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;


public class Entreprise {
	List<String> urlsA = new ArrayList<String>();

	public Entreprise(){
		
		WebDriver webDriver = new FirefoxDriver();

		webDriver.get("http://www.enterprise.fr/content/car_rental/popularLocations_fr_FR.html");
		
		List<WebElement> liens =webDriver.findElement(By.id("container")).findElements(By.tagName("a"));
		
		List<String> urls = new ArrayList<String>();
		 
		Iterator<WebElement> itlisteA = liens.iterator();
		
		while(itlisteA.hasNext()){
			
			WebElement baliseA = itlisteA.next();

			urls.add(baliseA.getAttribute("href"));

		}
		
		for(String url:urls){
			String urlParis = "http://www.enterprise.fr/car_rental/deeplinkmap.do?bid=3003&xparm=paris-location-de-voiture";
			if(! url.equals(urlParis)){
				
				webDriver.get(url);
			
			
			try{
				List<WebElement> listeTd = webDriver.findElement(By.xpath("//*[@id=\"listView\"]/table/tbody")).findElements(By.tagName("td"));
			
				
				 
				Iterator<WebElement> itlisteTd = listeTd.iterator();
		
				while(itlisteTd.hasNext()){
					
					WebElement baliseA = itlisteTd.next();
	
					urlsA.add(baliseA.getAttribute("href"));
	
				}
			
			}catch(org.openqa.selenium.NoSuchElementException e){
				
				
			}
			}
		}
		
		
		String urlParis = "http://www.enterprise.fr/car_rental/deeplinkmap.do?bid=3003&xparm=paris-location-de-voiture";
		String urlParis0 = "http://www.enterprise.fr/car_rental/customSearch.do?sortOrder=asc&sortColumn=0&startAt=0&searchCriteria=Paris&transactionId=WebTransaction24&tabView=listView&subsetQueryValue=";
		String urlParis10 = "http://www.enterprise.fr/car_rental/customSearch.do?sortOrder=asc&sortColumn=0&startAt=10&searchCriteria=Paris&transactionId=WebTransaction24&tabView=listView&subsetQueryValue=";
		String urlParis20 = "http://www.enterprise.fr/car_rental/customSearch.do?sortOrder=asc&sortColumn=0&startAt=20&searchCriteria=Paris&transactionId=WebTransaction24&tabView=listView&subsetQueryValue=";
		urls.clear();
		urls.add(urlParis);
		urls.add(urlParis0);
		urls.add(urlParis10);
		urls.add(urlParis20);
		
		
//		for(String url:urls){
//			
//				webDriver.get(url);
//			
//			try{
//				List<WebElement> listeTd = webDriver.findElement(By.xpath("//*[@id=\"listView\"]/table/tbody")).findElements(By.tagName("td"));
//				
//				
//				
//				Iterator<WebElement> itlisteTd = listeTd.iterator();
//				
//				while(itlisteTd.hasNext()){
//					
//					WebElement baliseA = itlisteTd.next();
//					
//					urlsA.add(baliseA.getAttribute("href"));
//					
//				}
//				
//			}catch(org.openqa.selenium.NoSuchElementException e){
//				
//				
//			}
//			
//		}
		
		
//		for(String urlA : urlsA){
//			System.out.println(urlsA.size());
//			webDriver.get(urlA);
//			System.out.println(webDriver.findElement(By.tagName("address")));
//			
//		}
		
		
	}
	
}
