import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;


public class CoteRoute {

	private WebDriver webDriver;
	private Agence agence;
	private String texteCSV = "NOM;ADRESSE;CP;LAT;LON;VILLE;TEL;EMAIL\r\n";

	
	public CoteRoute(){
		
		
		 webDriver = new FirefoxDriver();

		webDriver.get("http://www.coteroute.fr/centre-cote-route.html");
		
		Select droplist = new Select(webDriver.findElement(By.tagName("select")));   
		List<WebElement> listeOpR = webDriver.findElement(By.tagName("select")).findElements(By.tagName("option"));
		for(int j = 1 ; j< listeOpR.size() ; j++){

		droplist.selectByIndex(j);
		
		while(! isExist("ville"))
		{
		
		}
		List<WebElement> listeOp = webDriver.findElement(By.id("ville")).findElements(By.tagName("option"));
		
		Select droplist2 = new Select(webDriver.findElement(By.id("ville")));   
		
		for(int i = 1 ; i< listeOp.size() ; i++){
			
		agence= new Agence();
			
		droplist2.selectByIndex(i);
		
		while(! isExistX("//*[@id=\"ajax-content\"]/table/tbody/tr[1]/td[1]/div[2]"))
		{
		
		}
		
		String[] coordonnees = webDriver.findElement(By.xpath("//*[@id=\"ajax-content\"]/table/tbody/tr[1]/td[1]/div[2]")).getText().split("\n");
		
		agence.setAdresse(coordonnees[coordonnees.length -5]);

		
		if(testAllUpperCase(coordonnees[0]))
			agence.setNom(coordonnees[0]);
		
		if(testAllUpperCase(coordonnees[1]))
			agence.setNom( agence.getNom() +" "+ coordonnees[1]);
		
		if(! testAllUpperCase(coordonnees[coordonnees.length -6]))
		agence.setAdresse(agence.getAdresse() +" " + coordonnees[coordonnees.length -6]);


		
		agence.setCodePostale(coordonnees[coordonnees.length -3].substring(0, 6));
		agence.setVille( coordonnees[coordonnees.length -3].substring(6));
		agence.setTéléphone(coordonnees[coordonnees.length -2].substring(6).replaceAll("\\.", " "));
		
	
		 texteCSV+=agence.getNom() + ";";
		 texteCSV+=agence.getAdresse() + ";";
		 texteCSV+=agence.getCodePostale() + ";";
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
		       writer = new FileWriter("CoteRoute.csv", true);
		       writer.write(texte,0,texte.length());
		  }catch(IOException ex){
		      ex.printStackTrace();
		  }finally{
		    if(writer != null){
		       writer.close();
		    }
		  }
		}
		
	
	public boolean isExist(String tag)
	{
		try {
			webDriver.findElement(By.id(tag));
		}
		catch (Exception e) {
			return false;
		}
		
		
		return true;
	}
	public boolean isExistX(String tag)
	{
		try {
			webDriver.findElement(By.xpath(tag));
		}
		catch (Exception e) {
			return false;
		}
		
		
		return true;
	}
	
	public static boolean testAllUpperCase(String str){
		for(int i=0; i<str.length(); i++){
			char c = str.charAt(i);
			if(c >= 97 && c <= 122) {
				return false;
			}
		}
		//str.charAt(index)
		return true;
	}
}
