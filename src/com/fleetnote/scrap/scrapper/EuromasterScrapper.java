package com.fleetnote.scrap.scrapper;

import java.io.FileWriter;
import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.fleetnote.scrap.HibernateUtil;
import com.fleetnote.scrap.models.entity.Euromaster;
import com.fleetnote.scrap.webdriver.MyChromeWebDriver;


public class EuromasterScrapper {

	private static WebDriver mDriver;
	
	public static void main(String[] args) throws InterruptedException, IOException 
	{
		System.setProperty("webdriver.chrome.driver", "C:/env_dev/chromedriver.exe");
		mDriver = new MyChromeWebDriver();
		
		
		for(int i=34032;i<35000;i++)
		{
			Euromaster euroMaster = new Euromaster();
			Session session = HibernateUtil.currentSession();
			
			mDriver.get("http://centres.euromaster.fr/"+i);
			WebElement showPhone = mDriver.findElement(By.xpath("//*[@id='telfax']/div[1]"));
			showPhone.click();
			
			WebElement name = mDriver.findElement(By.xpath("//*[@id='posmaininfos']/div[1]/h1"));
			WebElement address = mDriver.findElement(By.xpath("//*[@id='posmaininfos']/div[1]/address"));
			WebElement tel = mDriver.findElement(By.xpath("//*[@id='telfax']/span[1]"));
			WebElement ville = mDriver.findElement(By.xpath("//*[@id='posmaininfos']/div[1]/address/span[3]"));
			WebElement cp = mDriver.findElement(By.xpath("//*[@id='posmaininfos']/div[1]/address/span[2]"));
			
			Double Lat = null;
			Double lng = null;
			if (mDriver instanceof JavascriptExecutor) {
				Lat = (Double) ((JavascriptExecutor) mDriver)
					.executeScript("return posMarkerCoordinates.lat;");
				
				lng = (Double) ((JavascriptExecutor) mDriver)
						.executeScript("return posMarkerCoordinates.lng;");
				
			}
			
			String codePostal =cp.getText();
			
			String euro = "";
			euroMaster.setIdEuromaster(i);
			euroMaster.setNom(name.getText().replace("\n", ""));
			euroMaster.setAdresse(address.getText());
			euroMaster.setCodePostal(codePostal);
			euroMaster.setLat(Lat);
			euroMaster.setLon(lng);
			euroMaster.setVille(ville.getText());
			euroMaster.setTéléphone(tel.getText().replace("Tel", ""));
			Transaction trans = session.beginTransaction();
			session.saveOrUpdate(euroMaster);
			trans.commit();
//			if(mDriver.findElements( By.className("lf_categorie") ).size() != 0)
//			{
//				List<WebElement> d = mDriver.findElements(By.className("lf_categorie"));
//				Iterator<WebElement> it = d.iterator();
//				while(it.hasNext())
//				{
//					WebElement el = it.next().findElement(By.tagName("img"));
//					
//					String img = el.getAttribute("src");
//					if(img.equals("https://leadformance-production-europe.s3-eu-west-1.amazonaws.com/smart_tag_parameters/1756/original.png?1364286021"));
//				}
//			}
//			euro +="\n";
//			ecrire(euro);
		}
	}
	
	private static void ecrire(String texte) throws IOException
    {
       
        FileWriter writer = null;
        try{
             writer = new FileWriter("aeuromaster.csv", true);
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
