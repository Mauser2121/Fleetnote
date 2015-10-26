package com.fleetnote.scrap.scrapper;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.fleetnote.scrap.models.entity.Agence;
import com.fleetnote.scrap.webdriver.MyHTMLWebDriver;




public class NationalCiter {

	Map<String,Agence> listeAgence = new HashMap<String, Agence>();
	private String texteCSV = "NOM;ADRESSE;CP;LAT;LON;VILLE;TEL;EMAIL\r\n";
	
	public NationalCiter(){
		
		MyHTMLWebDriver webDriver = new MyHTMLWebDriver();
		
		for(int i = 1 ; i<3 ;i++){
			
			webDriver.get("http://www.citer.fr/node/9?ville=toutes&type_agence=toutes&type_vehicule=tous&page=" +i + "\"");
			
			List<WebElement> listeTd = webDriver.findElements(By.className("tt_resultat"));
			
			List<String> urls = new ArrayList<String>();
			 
			Iterator<WebElement> itlisteTd = listeTd.iterator();
			
			while(itlisteTd.hasNext()){
				
				WebElement td = itlisteTd.next();
	
				urls.add(td.findElement(By.tagName("a")).getAttribute("href"));
	
			}
			
			
			
			for(String url:urls){
				Agence agence = new Agence();
				webDriver.get(url);
				
				String titre = webDriver.findElement(By.id("titre_page")).getText();
				titre.substring("Agence de location de voiture à".length());
				System.out.println( "TITRE = " + titre.substring("Agence de location de voiture à".length()) );
				
				String coordonnees = webDriver.findElement(By.className("col_g")).findElement(By.tagName("p")).getText();
				
				String[] tabCoordonnees = coordonnees.split("\n");
				String cpVille = tabCoordonnees[tabCoordonnees.length -1];
			    Matcher matcher = Pattern.compile("([0-9]*)").matcher(cpVille);
			    
			    if(matcher.find()){

					agence.setCodePostal(matcher.group(1));	
					agence.setVille(cpVille.substring(matcher.group(1).length()));
			    }
			    String adresse ="";
			    for(int j = 0 ; j< tabCoordonnees.length -1; j++){
			    	String coord = tabCoordonnees[j];
			    	if(coord.trim().length() != 0)
			    		adresse += coord ;	

			    }
			    agence.setAdresse(adresse);
				//System.out.println( "COORDONNEES = " + webDriver.findElement(By.className("col_g")).findElement(By.tagName("p")).getText() );
				String telFax = webDriver.findElement(By.className("col_d")).findElements(By.tagName("p")).get(1).getText();
				String tel = telFax.split("\n")[0].substring(6).replaceAll("\\.", " ");
				agence.setTéléphone(tel);
				
				 texteCSV+=agence.getNom() + ";";
				 texteCSV+=agence.getAdresse() + ";";
				 texteCSV+=agence.getCodePostal() + ";";
				 texteCSV+=agence.getLat() + ";";
				 texteCSV+=agence.getLon() + ";";
				 texteCSV+=agence.getVille() + ";";
				 texteCSV+=agence.getTéléphone() + ";";
				 texteCSV+=agence.getEmail() + "\n";
				
			}
		
		}
		
		 try {
				ecrire(texteCSV);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	private static void ecrire(String texte) throws IOException
	{
	  FileWriter writer = null;
	  try{
	       writer = new FileWriter("NationalCiter.csv", true);
	       writer.write(texte,0,texte.length());
	  }catch(IOException ex){
	      ex.printStackTrace();
	  }finally{
	    if(writer != null){
	       writer.close();
	    }
	  }
	}
}
