import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;


public class TotalGr {

	private WebDriver mDriver;
	
	public TotalGr(){
		System.setProperty("webdriver.chrome.driver","/Users/Erz/Documents/chromedriver.exe");
		
		ChromeOptions options = new ChromeOptions();
	    Map<String, Object> prefs = new HashMap<String, Object>();
	    prefs.put("profile.default_content_settings.popups", 0);
	    prefs.put("download.default_directory", "C:/Users/Erz/Documents/");
	    options.setExperimentalOptions("prefs", prefs);

	    options.addArguments("--test-type");
	    DesiredCapabilities cap = DesiredCapabilities.chrome();
	    cap.setCapability(ChromeOptions.CAPABILITY, options);
	    cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
	   
	    
		mDriver = new ChromeDriver(cap);
		connect("87234197002", "we-care12");
		getTransactions("01/01/2015","01/06/2015",true);
	}
	
	
	private void connect(String usrname, String pssword){
		mDriver.get("https://gronline.total.fr");
		WebElement username = mDriver.findElement(By.name("j_username"));
		WebElement password = mDriver.findElement(By.name("j_password"));
		
		username.sendKeys(usrname);
		password.sendKeys(pssword);
		
		WebElement id = mDriver.findElement(By.id("okbtn"));
		id.click();
	}
	
	public void getTransactions(String min, String max,Boolean allTransac){
		mDriver.get("https://gronline.total.fr/secure/clients/suivi/transactionrecherche.do?navRefClicked=nav.clients.suivi.transactionrecherche");
		
		WebElement dateMin = mDriver.findElement(By.name("criteres.dateTransactionMin"));
		WebElement dateMax = mDriver.findElement(By.name("criteres.dateTransactionMax"));
		dateMin.sendKeys(min);
		dateMax.sendKeys(max);
		
		Select selectFacturation =new Select(mDriver.findElement(By.name("natureTransaction")));
		if(allTransac)
			selectFacturation.selectByValue("3");
		else
			selectFacturation.selectByValue("1");
		
		mDriver.findElement(By.name("rechercher")).click();
		mDriver.get("https://gronline.total.fr/secure/clients/suivi/transactionresultat.do?method=exporterExcelTransaction");
	
	}
	
	
}
