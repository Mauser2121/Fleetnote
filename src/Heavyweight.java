import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Heavyweight {
	private HashMap<String, List<String>> datas;
	private List<String> allMarks;
	private final static String baseUrl = "http://www.europe-camions.com/";

	public static void main(String[] args) throws Exception {
		new Heavyweight();
	}

	public Heavyweight() throws Exception {
		WebDriver webDriver = new FirefoxDriver();
		allMarks = new ArrayList<String>();
		datas = new HashMap<String, List<String>>();
		getCamions(webDriver);
		getTracteurs(webDriver);
		getCamionRemorque(webDriver);
		getEnginDeVoirie(webDriver);
		getAutobus(webDriver);
		getAutocar(webDriver);
		getEnsembleRoutier(webDriver);
		persistThisMake();
		persistThisModels();
	}

	private void persistThisModels() throws Exception {
		Exporter exporter = new Exporter("./heavyweightModels.csv");
		
		exporter.addColumn("Marque");
		exporter.addColumn("description");
		exporter.addColumn("code");
		for (String curMake : allMarks) {
			if (!curMake.equals("frigo") && !curMake.equals("benne")
					&& !curMake.equals("fourgon") && !curMake.equals("citerne")) {
				for (String curModel : datas.get(curMake)) {
					List<String> toSend = new ArrayList<String>();
					toSend.add(curMake);
					toSend.add(curModel);
					toSend.add(curMake.substring(0, 3) + "_" + curModel.toLowerCase().trim().replace(' ', '_'));
					exporter.addData(toSend);
				}
			}
		}
		System.out.println("end");
		exporter.save();
	}

	private void persistThisMake() throws Exception {
		Exporter exporter = new Exporter("./heavyweightMake.csv");

		exporter.addColumn("codeTrMake");
		exporter.addColumn("descriptionTrMake");
		System.out.println("start");
		for (String curMark : allMarks) {
			if (!curMark.equals("frigo") && !curMark.equals("benne")
					&& !curMark.equals("fourgon") && !curMark.equals("citerne")) {
				List<String> toSend = new ArrayList<String>();
				toSend.add(curMark.substring(0, 3));
				toSend.add(curMark);
				exporter.addData(toSend);
			}
		}
		System.out.println("end");
		exporter.save();
	}

	private void getCamions(WebDriver webDriver) throws InterruptedException {
		webDriver.get(baseUrl
				+ "camion-occasion/marque-camion-occasion-f1-32.html");

		Thread.sleep(2000);
		List<WebElement> listElements = webDriver.findElements(By
				.xpath("//a[contains(@title, camion)]"));
		for (WebElement webElement : listElements) {
			String title = webElement.getAttribute("title");
			if (title.startsWith("camion")) {
				String[] splited = title.split("\\s+");
				String marque = splited[1];
				if (!datas.containsKey(marque)) {
					datas.put(marque, new ArrayList<String>());
					allMarks.add(marque);
				}
				String model = "";
				for (int i = 2; i < splited.length; i++) {
					model += splited[i] + " ";
				}
				if (!model.equals(""))
					if (!datas.get(marque).contains(model))
						datas.get(marque).add(model);
			}
		}
	}

	private void getTracteurs(WebDriver webDriver) throws InterruptedException {
		webDriver.get(baseUrl
				+ "tracteur-occasion/marque-tracteur-occasion-f1-31.html");

		Thread.sleep(2000);
		List<WebElement> listElements = webDriver.findElements(By
				.xpath("//a[contains(@title, tracteur)]"));
		for (WebElement webElement : listElements) {
			String title = webElement.getAttribute("title");
			if (title.startsWith("tracteur")) {
				String[] splited = title.split("\\s+");
				String marque = splited[1];
				if (!datas.containsKey(marque)) {
					datas.put(marque, new ArrayList<String>());
					allMarks.add(marque);
				}
				String model = "";
				for (int i = 2; i < splited.length; i++) {
					model += splited[i] + " ";
				}
				if (!model.equals(""))
					if (!datas.get(marque).contains(model))
						datas.get(marque).add(model);
			}
		}
	}

	private void getCamionRemorque(WebDriver webDriver)
			throws InterruptedException {
		webDriver
				.get(baseUrl
						+ "camion-remorque-occasion/marque-camion-remorque-occasion-f1-33.html");

		Thread.sleep(2000);
		List<WebElement> listElements = webDriver.findElements(By
				.xpath("//a[contains(@title, remorque)]"));
		for (WebElement webElement : listElements) {
			String title = webElement.getAttribute("title");
			if (title.startsWith("camion remorque")) {
				String[] splited = title.split("\\s+");
				String marque = splited[2];
				if (!datas.containsKey(marque)) {
					datas.put(marque, new ArrayList<String>());
					allMarks.add(marque);
				}
				String model = "";
				for (int i = 3; i < splited.length; i++) {
					model += splited[i] + " ";
				}
				if (!model.equals(""))
					if (!datas.get(marque).contains(model))
						datas.get(marque).add(model);
			}
		}
	}

	private void getEnginDeVoirie(WebDriver webDriver)
			throws InterruptedException {
		webDriver
				.get(baseUrl
						+ "engin-de-voirie-occasion/marque-engin-de-voirie-occasion-f1-103.html");

		Thread.sleep(2000);
		List<WebElement> listElements = webDriver.findElements(By
				.xpath("//a[contains(@title, voirie)]"));
		for (WebElement webElement : listElements) {
			String title = webElement.getAttribute("title");
			if (title.startsWith("engin de voirie")) {
				String[] splited = title.split("\\s+");
				String marque = splited[3];
				if (!datas.containsKey(marque)) {
					datas.put(marque, new ArrayList<String>());
					allMarks.add(marque);
				}
				String model = "";
				for (int i = 4; i < splited.length; i++) {
					model += splited[i] + " ";
				}
				if (!model.equals(""))
					if (!datas.get(marque).contains(model))
						datas.get(marque).add(model);
			}
		}
	}

	private void getEnsembleRoutier(WebDriver webDriver)
			throws InterruptedException {
		webDriver
				.get(baseUrl
						+ "ensemble-routier-occasion/marque-ensemble-routier-occasion-f1-34.html");

		Thread.sleep(2000);
		List<WebElement> listElements = webDriver.findElements(By
				.xpath("//a[contains(@title, voirie)]"));
		for (WebElement webElement : listElements) {
			String title = webElement.getAttribute("title");
			if (title.startsWith("ensemble routier")) {
				String[] splited = title.split("\\s+");
				String marque = splited[2];
				if (!datas.containsKey(marque)) {
					datas.put(marque, new ArrayList<String>());
					allMarks.add(marque);
				}
				String model = "";
				for (int i = 3; i < splited.length; i++) {
					model += splited[i] + " ";
				}
				if (!model.equals(""))
					if (!datas.get(marque).contains(model))
						datas.get(marque).add(model);
			}
		}
		for (String mark : allMarks) {
			for (String model : datas.get(mark)) {
				System.out.println("Marque = " + mark + "    model = " + model);
			}
		}
	}

	private void getAutobus(WebDriver webDriver) throws InterruptedException {
		webDriver.get(baseUrl
				+ "autobus-occasion/marque-autobus-occasion-f1-29.html");

		Thread.sleep(2000);
		List<WebElement> listElements = webDriver.findElements(By
				.xpath("//a[contains(@title, autobus)]"));
		for (WebElement webElement : listElements) {
			String title = webElement.getAttribute("title");
			if (title.startsWith("autobus")) {
				String[] splited = title.split("\\s+");
				String marque = splited[1];
				if (!datas.containsKey(marque)) {
					datas.put(marque, new ArrayList<String>());
					allMarks.add(marque);
				}
				String model = "";
				for (int i = 2; i < splited.length; i++) {
					model += splited[i] + " ";
				}
				if (!model.equals(""))
					if (!datas.get(marque).contains(model))
						datas.get(marque).add(model);
			}
		}
	}

	private void getAutocar(WebDriver webDriver) throws InterruptedException {
		webDriver.get(baseUrl
				+ "autocar-occasion/marque-autocar-occasion-f1-30.html");

		Thread.sleep(2000);
		List<WebElement> listElements = webDriver.findElements(By
				.xpath("//a[contains(@title, autobus)]"));
		for (WebElement webElement : listElements) {
			String title = webElement.getAttribute("title");
			if (title.startsWith("autobus")) {
				String[] splited = title.split("\\s+");
				String marque = splited[1];
				if (!datas.containsKey(marque)) {
					datas.put(marque, new ArrayList<String>());
					allMarks.add(marque);
				}
				String model = "";
				for (int i = 2; i < splited.length; i++) {
					model += splited[i] + " ";
				}
				if (!model.equals(""))
					if (!datas.get(marque).contains(model))
						datas.get(marque).add(model);
			}
		}
	}
}
