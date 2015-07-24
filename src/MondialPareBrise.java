import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;


public class MondialPareBrise {

	public static void main(String[] args) {
		new MondialPareBrise();
	}
	
	Map<String,Agence> listeAgence = new HashMap<String, Agence>();
	private String texteCSV = "NOM;ADRESSE;CP;LAT;LON;VILLE;TEL;EMAIL\r\n";
	public MondialPareBrise(){
		
		WebDriver webDriver = new FirefoxDriver();
		
		
		for(int i = 20 ; i< 96 ; i++){
			
			
			if(i<10)
			webDriver.get("http://www.mondialparebrise.fr/result-ifr-0"+ i +".asp");
			else
			webDriver.get("http://www.mondialparebrise.fr/result-ifr-"+ i +".asp");	
			
			try{
				
			List<WebElement> tds = webDriver.findElement(By.xpath("//*[@id=\"page\"]/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/table[2]/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td[2]/table/tbody/tr/td/table/tbody/tr[2]/td/table/tbody/tr/td[1]/table/tbody/tr/td/table/tbody")).findElements(By.tagName("td"));
		
		
			Iterator<WebElement> itdivs = tds.iterator();
			
			while(itdivs.hasNext()){
				Agence agence = new Agence();
				WebElement contenuTd = itdivs.next();
				
				String coord = contenuTd.getText();
				
				if(coord.split("\n").length == 4){
					
					agence.setNom("MONDIALPAREBRISE - " + coord.split("\n")[0].replaceAll(":", ""));
					agence.setAdresse( coord.split("\n")[1]);
					agence.setCodePostale(coord.split("\n")[2].substring(0, 6));
					agence.setVille( coord.split("\n")[2].substring(6));
					agence.setTéléphone( coord.split("\n")[3].substring(5, 20).replaceAll("\\.", " "));
					listeAgence.put(agence.getAdresse(), agence);

				}
				
			
			
			}
			
			
			
			}catch(org.openqa.selenium.NoSuchElementException e){
				
				System.out.println("ER 404");
			}
			
			
		
		}
		
	
		
		Collection<Agence> valeurs = listeAgence.values();
		Iterator it = valeurs.iterator();
		while (it.hasNext()){
		   Agence agc = (Agence) it.next();
		   
			 texteCSV+=agc.getNom() + ";";
			 texteCSV+=agc.getAdresse() + ";";
			 texteCSV+=agc.getCodePostale() + ";";
			 texteCSV+=agc.getLat() + ";";
			 texteCSV+=agc.getLon() + ";";
			 texteCSV+=agc.getVille() + ";";
			 texteCSV+=agc.getTéléphone() + ";";
			 texteCSV+=agc.getEmail() + "\n";
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
       writer = new FileWriter("MondialParebrise.csv", true);
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
