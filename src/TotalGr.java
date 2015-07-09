import java.util.HashMap;
import java.util.List;
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
		getTID();
		getTransactions("01/01/2015","01/06/2015",true);
		getCards();
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
	
	public void getCards(){
		mDriver.get("https://gronline.total.fr/secure/clients/supports/recherche.do?navRefClicked=nav.clients.supports.recherche");
		WebElement nouveau = mDriver.findElement(By.xpath("//*[@id='search-main-content']/div[2]/div[2]/table/tbody/tr[3]/td[2]/table/tbody/tr/td[1]/input"));	
		WebElement fab = mDriver.findElement(By.xpath("//*[@id='search-main-content']/div[2]/div[2]/table/tbody/tr[3]/td[2]/table/tbody/tr/td[2]/input"));
		WebElement actif = mDriver.findElement(By.xpath("//*[@id='search-main-content']/div[2]/div[2]/table/tbody/tr[3]/td[2]/table/tbody/tr/td[3]/input"));
		WebElement renouvellement = mDriver.findElement(By.xpath("//*[@id='search-main-content']/div[2]/div[2]/table/tbody/tr[3]/td[2]/table/tbody/tr/td[4]/input"));
		WebElement arenouveler = mDriver.findElement(By.xpath("//*[@id='search-main-content']/div[2]/div[2]/table/tbody/tr[3]/td[2]/table/tbody/tr/td[5]/input"));
		WebElement annule = mDriver.findElement(By.xpath("//*[@id='search-main-content']/div[2]/div[2]/table/tbody/tr[3]/td[2]/table/tbody/tr/td[6]/input"));
		WebElement oppose = mDriver.findElement(By.xpath("//*[@id='search-main-content']/div[2]/div[2]/table/tbody/tr[3]/td[2]/table/tbody/tr/td[7]/input"));
		
		fab.click();
		renouvellement.click();
		
		WebElement rechercher = mDriver.findElement(By.name("rechercher"));
		rechercher.click();
		mDriver.get("https://gronline.total.fr/secure/clients/supports/resultatrecherche.do?method=exporterExcelEtatDeParc");
	}
	
	
	public void getTID(){
		mDriver.get("https://gronline.total.fr/secure/clients/rapports/telechargementtid.do?navRefClicked=nav.clients.rapports.transactionsfacturees");
		
		List<WebElement> lines = mDriver.findElements(By.xpath("//*[@id='tidClientPR']/tbody/tr"));
		
		for(WebElement line : lines){
			System.out.println(line.findElement(By.tagName("a")).getAttribute("href"));
		}
		
	}
	
	
}
