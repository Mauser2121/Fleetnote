import java.io.FileWriter;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import webdriver.MyWebDriver;


public class Oscaro {
	private static WebDriver mDriver;
	
	public static void main(String[] args) throws InterruptedException, IOException 
	{
		new Oscaro();
	}
	
	public Oscaro() throws InterruptedException{
		
		mDriver = new MyWebDriver();
		
		String sortie="";
		for(int i=1;i<1000;i++)
		{
						sortie = "";
						mDriver.get("http://www.oscaro.com");
			
						String script = "$('.divMoteurPlaque_Inactif').click()";
						if (mDriver instanceof JavascriptExecutor) {
							((JavascriptExecutor) mDriver)
								.executeScript(script);
						}
						WebElement immat = mDriver.findElement(By.xpath("//*[@id='tbPlaqueImmat']"));
						WebElement name = mDriver.findElement(By.xpath("//*[@id='tbNomImmat']"));
						
						String plaque = "AB";
						
						plaque = plaque.concat(String.format("%03d", i));
						plaque = plaque.concat("AA");
						immat.sendKeys(plaque);
						name.sendKeys("oui");
						if (mDriver instanceof JavascriptExecutor) {
									((JavascriptExecutor) mDriver)
										.executeScript("$('#btn_plaque_ok').click();");
								}
						Thread.sleep(2000);
						WebElement marque = mDriver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[3]/td/div/div/table/tbody/tr/td[2]/a"));
						WebElement version = mDriver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[3]/td/div/div/table/tbody/tr[2]/td/a"));
						
						sortie+=plaque + ";";
						sortie+=marque.getText()+";";
						sortie+=version.getText()+";";
						sortie+=marque.getAttribute("href")+"\n";
						
						try {
							ecrire(sortie);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
						
						
						
			}
						
					
					
	}
	
	private static void ecrire(String texte) throws IOException
    {
        FileWriter writer = null;
        try{
             writer = new FileWriter("Oscaro.csv", true);
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
