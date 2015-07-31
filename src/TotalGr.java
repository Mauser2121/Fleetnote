import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Password;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;

import webdriver.FileDownloader;


public class TotalGr {

	private WebDriver mDriver;
	private List<Password> passwords = new ArrayList<Password>();
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");///TODO : Déporter dans une librairie ou passez à JAVA 7 ou 8
	DateFormat df2 = new SimpleDateFormat("dd.MM.yyyy");
	public TotalGr(){
		System.setProperty("webdriver.chrome.driver","/env_dev/chromedriver.exe");
		
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
		passwords.add(new Password("Lafarge", "87782862005 ","we-care12"));
		passwords.add(new Password("Caldeo","87234197002","we-care12"));
		
		Calendar calMax = Calendar.getInstance();
		calMax.set(Calendar.DATE,1);
		calMax.set(Calendar.MONTH,6);
		calMax.set(Calendar.YEAR,2015);
		calMax.set(Calendar.DAY_OF_MONTH, 1);
		
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE,1);
		cal.set(Calendar.MONTH,0);
		cal.set(Calendar.YEAR,2015);
		cal.set(Calendar.DAY_OF_MONTH, 1);
	
		
		for (int i = 0; i < passwords.size(); i++) {
			
			connect(passwords.get(i).getLogin(), passwords.get(i).getPassword());
			
			
			while(cal.getTime().before(calMax.getTime())){
				Date min = cal.getTime();  
				cal.add(cal.DATE, 5);
				Date max = cal.getTime();
				String filename = passwords.get(i).getName()
						  +df2.format(min)
						  +"_"+df2.format(max)							
						  +".xls";
				getTransactions(df.format(min),df.format(max),filename,true);
				cal.add(cal.DATE, 1);
			}
//			for(int j=0;j<7;j++){
//				cal.set(Calendar.DAY_OF_MONTH, 1);
//				int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
//				
//				for(int k=0;k<Math.round(daysInMonth/5);k++)
//				{
//					
//				}
//				Date firstDayOfMonth = cal.getTime();  
//				
//				
//				cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
//				Date lastDayOfMonth = cal.getTime();
//				String filename = passwords.get(i).getName()
//								  +cal.get(cal.MONTH)
//								  +"_"+cal.get(cal.YEAR)							
//								  +".xls";
//				getTransactions(df.format(firstDayOfMonth),df.format(lastDayOfMonth),filename,true);
//				cal.add(Calendar.MONTH, 1);
//		
//			}
		
			getCards();
		}
		
		
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
	
	public void getTransactions(String min, String max,String filename,Boolean allTransac){
		mDriver.get("https://gronline.total.fr/secure/clients/suivi/transactionrecherche.do?navRefClicked=nav.clients.suivi.transactionrecherche");
		
		WebElement dateMin = mDriver.findElement(By.name("criteres.dateTransactionMin"));
		WebElement dateMax = mDriver.findElement(By.name("criteres.dateTransactionMax"));
		dateMin.clear();
		dateMin.sendKeys(min);
		
		dateMax.clear();
		dateMax.sendKeys(max);
		
		Select selectFacturation =new Select(mDriver.findElement(By.name("natureTransaction")));
		if(allTransac)
			selectFacturation.selectByValue("3");
		else
			selectFacturation.selectByValue("1");
		
		mDriver.findElement(By.name("rechercher")).click();
		
		FileDownloader downloadTestFile = new FileDownloader(mDriver);
//		mDriver.get("https://gronline.total.fr/secure/clients/suivi/transactionresultat.do?method=exporterExcelTransaction");
		try {
			downloadTestFile.downloadFile("https://gronline.total.fr/secure/clients/suivi/transactionresultat.do?method=exporterExcelTransaction",
										  filename);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
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