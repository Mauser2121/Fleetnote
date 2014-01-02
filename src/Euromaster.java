import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;


public class Euromaster {

	private static WebDriver mDriver;
	
	public static void main(String[] args) throws InterruptedException, IOException 
	{
		mDriver = new FirefoxDriver();
		
		for(int i=34031;i<35000;i++)
		{
			mDriver.get("http://centres.euromaster.fr/"+i);
			WebElement name = mDriver.findElement(By.xpath("//*[@id='posmaininfos']/div[1]/h1"));
			WebElement address = mDriver.findElement(By.xpath("//*[@id='posmaininfos']/div[1]/address"));
			WebElement tel = mDriver.findElement(By.xpath("//*[@id='telfax']/span[1]"));
			WebElement ville = mDriver.findElement(By.xpath("//*[@id='posmaininfos']/div[1]/address/span"));
			
			Double Lat = null;
			Double lng = null;
			if (mDriver instanceof JavascriptExecutor) {
				Lat = (Double) ((JavascriptExecutor) mDriver)
					.executeScript("return posMarkerCoordinates.lat;");
				
				lng = (Double) ((JavascriptExecutor) mDriver)
						.executeScript("return posMarkerCoordinates.lng;");
				
			}
			String cp = "";
			cp = ville.getText().split(" ")[0];
			
			String euro = "";
			euro =  name.getText().replace("\n", "")+";";
			euro += address.getText().replace("\n", "")+";";
			euro += cp+";";
			euro += Lat+";";
			euro += lng+";";
			euro += ville.getText().substring(cp.length()+1)+";";
			euro += tel.getText().replace("Tel", "")+";";
			euro += ""+";";
			
			if(mDriver.findElements( By.className("lf_categorie") ).size() != 0)
			{
				List<WebElement> d = mDriver.findElements(By.className("lf_categorie"));
				Iterator<WebElement> it = d.iterator();
				while(it.hasNext())
				{
					WebElement el = it.next().findElement(By.tagName("img"));
					
					String img = el.getAttribute("src");
					if(img.equals("https://leadformance-production-europe.s3-eu-west-1.amazonaws.com/smart_tag_parameters/1756/original.png?1364286021"));
				}
			}
			euro +="\n";
			ecrire(euro);
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