import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;


public class Rentacar {
	
	private List<Agence> listAgence = new ArrayList<Agence>();
	private String texteCSV ;	
	
	public Rentacar(){
		
		WebDriver webdriver ;
		webdriver = new FirefoxDriver();
		webdriver.get("http://www.rentacar.fr/agences");		
		
		List<WebElement> liens = webdriver.findElement(By.id("listAgenceNearContent")).findElements(By.tagName("a"));
		
		List<String> urls = new ArrayList<String>();
		 
		Iterator<WebElement> itdivs = liens.iterator();
		
		while(itdivs.hasNext()){
			
			WebElement div = itdivs.next();

			urls.add(div.getAttribute("href"));

		}
		
		
		
		for(String url:urls){
			
			Agence agence = new Agence();
			webdriver.get(url);
			
			List<WebElement> listP =  webdriver.findElement(By.className("ofc_add")).findElements(By.tagName("p"));
			
			String nameAgence = listP.get(0).getText() ;
			String addA = listP.get(1).getText() ;
			String cpA = listP.get(2).getText().substring(0, 5) ;
			String villeA = listP.get(2).getText().substring(6) ;
			
			String telephone = webdriver.findElement(By.className("red_txt")).getText();
			
			
			  agence.setNom(nameAgence);
			  agence.setAdresse(addA);
			  agence.setCodePostale(cpA);
			  agence.setVille(villeA);
			  agence.setTéléphone(telephone);
			  
			  listAgence.add(agence);
			  
				 texteCSV+=agence.getNom() + ";";
				 texteCSV+=agence.getAdresse() + ";";
				 texteCSV+=agence.getCodePostale() + ";";
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
             writer = new FileWriter("Rentacar.csv", true);
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
