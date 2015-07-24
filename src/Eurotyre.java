import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.xpath.XPathContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;


public class Eurotyre {

	private String texteCSV = "NOM;ADRESSE;CP;LAT;LON;VILLE;TEL;EMAIL\r\n";
	
	public static void main(String[] args) throws InterruptedException, IOException 
	{
		new Eurotyre();
	}
	
	
	public Eurotyre(){
		
		System.out.println(new XPathContext().getClass().getProtectionDomain().getCodeSource().getLocation());
		
		WebDriver webDriver = new FirefoxDriver();
		
		webDriver.get("http://www.eurotyre.fr/+-Ain-+.html");	
		
		List<WebElement> liens = webDriver.findElement(By.className("departement")).findElements(By.tagName("li"));

		List<String> urls = new ArrayList<String>();
		 
		Iterator<WebElement> itliens = liens.iterator();
		
		while(itliens.hasNext()){
			
			WebElement baliseLi = itliens.next();

			//urls.add(baliseA.getAttribute("href"));
		    Matcher matcher = Pattern.compile("\\([0-9]*\\)").matcher(baliseLi.getText());
		    
		    if(matcher.find()){
		    	
		    	if( ! matcher.group().equals("(0)") ){
		    			
		    		urls.add( baliseLi.findElement(By.tagName("a")).getAttribute("href"));
		    	}
			
		    }
		}
		
		for(String url:urls){
			
			Agence agence = new Agence();
			webDriver.get(url);
			
			agence.setLat(  webDriver.findElement(By.className("bloc_principal_liste")).getAttribute("lat") );
			agence.setLon(  webDriver.findElement(By.className("bloc_principal_liste")).getAttribute("lon") );
			
			agence.setNom( webDriver.findElement(By.tagName("h3")).getText() );
			agence.setAdresse( webDriver.findElement(By.className("resultat_magasin")).findElements(By.tagName("p")).get(0).getText());
			String adresse =  webDriver.findElement(By.className("resultat_magasin")).findElements(By.tagName("p")).get(1).getText();
			
			agence.setCodePostale( adresse.substring(0, 6) );
			agence.setVille(  adresse.substring(6));
			
			 texteCSV+=agence.getNom() + ";";
			 texteCSV+=agence.getAdresse() + ";";
			 texteCSV+=agence.getCodePostale() + ";";
			 texteCSV+=agence.getLat() + ";";
			 texteCSV+=agence.getLon() + ";";
			 texteCSV+=agence.getVille() + ";";
			 texteCSV+=agence.getTéléphone() + ";";
			 texteCSV+=agence.getEmail() + "\n";
	
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
            writer = new FileWriter("Eurotyre2.csv", true);
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
