import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;



public class UcarScrapper {

	private List<Agence> listAgence = new ArrayList<Agence>();
	private List<String> listAgenceNonV = new ArrayList<String>();
	private String texteCSV ;

	
	public static void main(String[] args){
		//new UcarScrapper();
		new Rentacar();
	}
	
	public UcarScrapper(){
		WebDriver webdriver ;
		
		webdriver = new FirefoxDriver();
		webdriver.get("http://ucar.fr/agences");

		List<String> urls = new ArrayList<String>();
		List<String> urlsAgence = new ArrayList<String>();
		
		List<WebElement> divs = webdriver.findElements(By.className("name_dept"));
		 
		Iterator<WebElement> itdivs = divs.iterator();
		
		while(itdivs.hasNext()){
			
			WebElement div = itdivs.next();

			urls.add(div.findElement(By.tagName("a")).getAttribute("href"));

		}
		
		 for(String url:urls)
		 {

			 	webdriver.get( url );
			 	if(webdriver.findElements(By.id("titrTab2"))!=null){
					urlsAgence = new ArrayList<String>();
				 	List<WebElement> lienAgence = webdriver.findElements(By.id("titrTab2"));
					Iterator<WebElement> itlienAgence = lienAgence.iterator();
					
					while(itlienAgence.hasNext()){
						WebElement div = itlienAgence.next();
						urlsAgence.add(div.findElement(By.tagName("a")).getAttribute("href"));
					}
					
					 for(String urlAgence:urlsAgence)
					 { 
						Agence agence = new Agence(); 	
						webdriver.get(urlAgence);
							
							try{
								String addAgence =  webdriver.findElement(By.id("coordon")).findElements(By.tagName("p")).get(0).getText();
								String telMail =  webdriver.findElement(By.id("coordon")).findElements(By.tagName("p")).get(1).getText();
								String nameAgence =  webdriver.findElement(By.id("titre")).findElements(By.tagName("h1")).get(0).getText();
								String addA = addAgence.split(",")[0];
								String cpA = addAgence.split(",")[1];
								String villeA = addAgence.split(",")[2];
								
							    Matcher matcher = Pattern.compile("Tel : ([0-9]{2}\\s?[0-9]{2}\\s?[0-9]{2}\\s?[0-9]{2}\\s?[0-9]{2})").matcher(telMail);
							    	String telephone = null;
							      if(matcher.find()){
							    	  telephone =  matcher.group(1);
							      }
							      telMail = telMail.replaceAll("[\\s]", " ");
						
							      Pattern patt = Pattern.compile("[\\S]*@[\\S]*");
							      Matcher matcher2 = patt.matcher(telMail);
							      
							      String mail = null;
							      
							      if(matcher2.find()){
							    	  mail = matcher2.group();
							      }
								  agence.setNom(nameAgence);
								  agence.setAdresse(addA);
								  agence.setCodePostale(cpA);
								  agence.setVille(villeA);
								  agence.setTéléphone(telephone);
								  agence.setEmail(mail);
								  
								  listAgence.add(agence);
						 
							}catch(org.openqa.selenium.NoSuchElementException e){
								
								listAgenceNonV.add(urlAgence);
							}
							
					 	}
				 }
		 }

		 
		 
		 for(Agence agence :listAgence ){
			 
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
             writer = new FileWriter("Ucar.csv", true);
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
