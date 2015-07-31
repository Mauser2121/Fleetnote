import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import webdriver.Exporter;


public class Siligom {
	private static WebDriver mDriver;
	private static Exporter exporter;
	
	public static void main(String[] args) throws InterruptedException {

		mDriver = new FirefoxDriver();
		
		String firstGoogleSearch = "file:///C:/env_dev/workspace/FleetNoteScrap/scrap%20siligom1.html";
		String secondGoogleSearch = "file:///C:/env_dev/workspace/FleetNoteScrap/scrap%20siligom2.html";
		List<String> allUrls = new ArrayList<String>();
		
		mDriver.get(firstGoogleSearch);
		List<WebElement> allLinks = mDriver.findElements(By.xpath("//*[@class='srg']/li/div/h3/a"));
		allLinks.remove(0);
		for (WebElement webElement : allLinks) {
			allUrls.add(webElement.getAttribute("href"));
		}
		mDriver.get(secondGoogleSearch);
		allLinks = mDriver.findElements(By.xpath("//*[@class='srg']/li/div/h3/a"));
		System.out.println("start loop");
		for (WebElement webElement : allLinks) {
			allUrls.add(webElement.getAttribute("href"));
		}
		
		exporter = new Exporter("./Siligom.csv");
		try {
			exporter.addColumn("name");
			exporter.addColumn("address");
			exporter.addColumn("postal code");
			exporter.addColumn("city");
			exporter.addColumn("phone");
			exporter.addColumn("lattitude");
			exporter.addColumn("longitude");
		} catch (Exception e) {
			e.printStackTrace();
			return ;
		}
		
		for (String url : allUrls) {
			getInfoInSite(url);
		}
		try {
			exporter.save();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void getInfoInSite(String url) {
		String name;
		String address;
		String postalcode;
		String city;
		String phone;
		Double lattitude = 0.0;
		Double longitude = 0.0;
		List<String> toSend;
		String coordonnees;
		String[] organiazedCoordonnees;
		String latAndLng;
		
		mDriver.get(url);
		
		WebElement tmpElement = null;
		
		try {
			tmpElement = mDriver.findElement(By.xpath("//*[@class='titreCentre']")); 
		} catch (Exception e) {
			return;
		}
		name = tmpElement.getText().trim();
		System.out.println(name + "=======");
		coordonnees = mDriver.findElement(By.xpath("//*[@class='coordonneesCentre']")).getText().trim();
		organiazedCoordonnees = coordonnees.split("\n");
		address = organiazedCoordonnees[2];
		if (organiazedCoordonnees[3].length() < 8)
			return ;
		postalcode = organiazedCoordonnees[3].substring(0, 5);
		city = organiazedCoordonnees[3].substring(6);
		if (organiazedCoordonnees[5].length() < 4)
			return ;
		phone = organiazedCoordonnees[5].substring(3);
		
		latAndLng = mDriver.findElement(By.xpath("//*[@class='itineraire']/a")).getAttribute("href");
		lattitude = Double.valueOf(latAndLng.substring(latAndLng.indexOf("q=")+2, latAndLng.indexOf(",")));
		longitude = Double.valueOf(latAndLng.substring(latAndLng.indexOf(",")+1, latAndLng.indexOf("&hl")));
		System.out.println(longitude);
		
		toSend = new ArrayList<String>();
		toSend.add(name);
		toSend.add(address);
		toSend.add(postalcode);
		toSend.add(city);
		toSend.add(phone);
		toSend.add(lattitude.toString());
		toSend.add(longitude.toString());
		try {
			exporter.addData(toSend);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
