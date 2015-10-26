package com.fleetnote.scrap.scrapper;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By.ByClassName;
import org.openqa.selenium.firefox.FirefoxDriver;


public class NeoWebCar {

	private static WebDriver mDriver;
	private static WebDriver mDriver2;
	
	public static void main(String[] args) throws InterruptedException, IOException 
	{
		mDriver = new FirefoxDriver();
		mDriver2 = new FirefoxDriver();
		
		for(int i =0;i<3;i++)
		{
		mDriver.get("http://www.neowebcar.com/concessionnaires/marque/opel?offset="+i);
		
		
		String lignesortie;
		
		List<WebElement> laddresses = mDriver.findElements(By.className("showcase"));
		Iterator<WebElement> it = laddresses.iterator();
		while(it.hasNext())
		{
			lignesortie = new String();
			WebElement parent = it.next();
			WebElement data =  parent.findElement(By.xpath("div[@class='address']"));
			WebElement url = parent.findElement(By.xpath("div[@class='logo']/a"));
			
			if(data !=null)
			{
				List<WebElement> laddress = data.findElements(By.tagName("p"));
				Iterator<WebElement> it2 = laddress.iterator();
				while(it2.hasNext())
				{
					WebElement d = it2.next();
					lignesortie += d.getText()+";";
				}
				
				String pagegmap = url.getAttribute("href");
				mDriver2.get(pagegmap);
				if(pagegmap!="")
				{
					WebElement urlgmap = mDriver2.findElement(By.xpath("//*[@id='map']/div/div[7]/div[2]/a"));
					String coord = urlgmap.getAttribute("href");
					coord = coord.split("\\?")[1].split("&")[0].replace("ll=", "");
					lignesortie += coord+";\n";
					ecrire(lignesortie);
				}
				else
				{
					lignesortie += ""+";\n";
					ecrire(lignesortie);
				}
			}
			
		}
		}
	}
	
	private static void ecrire(String texte) throws IOException
    {
        FileWriter writer = null;
        try{
             writer = new FileWriter("Opel.csv", true);
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
