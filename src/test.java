import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


public class test {

	
	public static void main(String[] args) throws InterruptedException, IOException 
	{
		WebDriver mDriver = new FirefoxDriver();
		
		mDriver.get("http://ucar.fr/agences/UCAR_TREVOUX-938.html");
		
		
	}
}
