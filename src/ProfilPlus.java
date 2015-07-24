import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class ProfilPlus {

	private static WebDriver mDriver;
	private static Exporter exporter;

	public static void main(String[] args) throws InterruptedException {

		mDriver = new FirefoxDriver();

		String getByCityTemplate = "http://agence.profilplus.fr/search?page=XXXTOREPLACEXXX&query=France&st_like%5BCATEGORIE_DE_VEHICULE%5D%5B%5D=";
		String xPathLastSeeDetails = "//*[@class='last see-details']";
		String fullUrlSearch;

		exporter = new Exporter("./ProfilPlus.csv");
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
		}

		for (int i = 1; i <= 10; ++i) {
			fullUrlSearch = getByCityTemplate;
			fullUrlSearch = fullUrlSearch.replace("XXXTOREPLACEXXX", "" + i);
			mDriver.get(fullUrlSearch);
			List<WebElement> toDaNextStepah = null;
			try {
				toDaNextStepah = mDriver.findElements(By
						.xpath(xPathLastSeeDetails));
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (toDaNextStepah != null && toDaNextStepah.isEmpty() == false) {
				getParticulary(toDaNextStepah);
			}
		}
		try {
			exporter.save();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void getParticulary(List<WebElement> toDaNextStepah)
			throws InterruptedException {
		List<String> url;
		String name;
		String address;
		String postalcode;
		String city;
		String phone;
		Double lattitude = 0.0;
		Double longitude = 0.0;
		List<String> toSend;

		url = new ArrayList<String>();
		for (WebElement webElement : toDaNextStepah) {
			url.add(webElement.getAttribute("href"));
		}
		for (String curUrl : url) {
			mDriver.get(curUrl);
			// System.out.println("waintin'...");
			// Thread.sleep(5000);
			// System.out.println("stahp waitin'");

			if (mDriver instanceof JavascriptExecutor) {
				lattitude = (Double) ((JavascriptExecutor) mDriver)
						.executeScript("return posMarkerCoordinates.lat;");

				longitude = (Double) ((JavascriptExecutor) mDriver)
						.executeScript("return posMarkerCoordinates.lng;");

			}
			name = mDriver
					.findElement(By.xpath("//*[@id='posmaininfos']/div[1]/h1"))
					.getText().trim();
			phone = mDriver.findElement(By.xpath("//*[@class='lf_phone']"))
					.getText().substring(4);
			postalcode = mDriver
					.findElement(By.xpath("//*[@id='telfax']/address/span"))
					.getText().substring(0, 5);
			city = mDriver
					.findElement(By.xpath("//*[@id='telfax']/address/span"))
					.getText().substring(6);
			address = mDriver
					.findElement(By.xpath("//*[@id='telfax']/address"))
					.getText();
			address = address.replace(postalcode + " " + city, "").trim();
			address = address.replace('\n', ' ');
			System.out.println("address : " + address + "\n" + "postal code : "
					+ postalcode + "\ncity : " + city + "\nphone : " + phone
					+ "\nlattitude : " + lattitude + "\nlongitude : "
					+ longitude + "\n==========================\n\n\n");
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
}