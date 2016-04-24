package com.fleetnote.scrap.scrapper;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import com.fleetnote.scrap.webdriver.Exporter;
import com.fleetnote.scrap.webdriver.MyHTMLWebDriver;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.google.common.base.Predicate;
import com.thoughtworks.selenium.condition.Presence;

public class Speedy {
	private static WebDriver mDriver;
	private static Exporter exporter;

	public static void main(String[] args) throws InterruptedException {
		mDriver = new MyHTMLWebDriver();

		
		String urlHome = "http://centres-auto.speedy.fr/";
		String tmpUrl;
		List<String> url;

		exporter = new Exporter("./Speedy.csv");
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

		url = new ArrayList<String>();
		for (int i = 6; i < 8; ++i) {
			mDriver.get(urlHome);
			if (i >= 10)
				mDriver.findElement(By.xpath("//*[@id='lieu']"))
						.sendKeys("" + i + Keys.ENTER);
			else
				mDriver.findElement(By.xpath("//*[@id='lieu']")).sendKeys(
						"0" + i + Keys.ENTER);
			Thread.sleep(2000);
			List<WebElement> toDaNextStepah = mDriver.findElements(By
					.xpath("//*[@class='detail_centre']"));
			for (WebElement webElement : toDaNextStepah) {
				tmpUrl = webElement.getAttribute("href");
				if (url.isEmpty())
					url.add(tmpUrl);
				else if (!verifyMultiple(tmpUrl, url))
					url.add(tmpUrl);
			}
		}
		getParticulary(url);
		try {
			exporter.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void getParticulary(List<String> url)
			throws InterruptedException {
		String name;
		String address;
		String postalcode;
		String city;
		String phone;
		Double lattitude = 0.0;
		Double longitude = 0.0;
		List<String> toSend;

		for (String curString : url) {
			System.out.println(curString);
		}

		for (String curUrl : url) {
			System.out.println(curUrl);
			Thread.sleep(2000);
			mDriver.get(curUrl);
			name = mDriver.findElement(By.xpath("//*[@itemprop='name']"))
					.getText();
			phone = mDriver.findElement(By.xpath("//*[@itemprop='telephone']"))
					.getText();
			postalcode = mDriver.findElement(
					By.xpath("//*[@itemprop='postalCode']")).getText();
			city = mDriver.findElement(
					By.xpath("//*[@itemProp='addressLocality']")).getText();
			address = mDriver.findElement(
					By.xpath("//*[@itemProp='streetAddress']")).getText();
			address = address.replace(postalcode + " " + city, "").trim();
			address = address.replace('\n', ' ');

			lattitude = Double.parseDouble(mDriver.findElement(
					By.xpath("//*[@itemprop='latitude']")).getAttribute(
					"content"));
			longitude = Double.parseDouble(mDriver.findElement(
					By.xpath("//*[@itemprop='longitude']")).getAttribute(
					"content"));

			System.out.println("name : " + name + "\naddress : " + address
					+ "\n" + "postal code : " + postalcode + "\ncity : " + city
					+ "\nphone : " + phone + "\nlattitude : " + lattitude
					+ "\nlongitude : " + longitude
					+ "\n==========================\n\n\n");
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

	private static boolean verifyMultiple(String string, List<String> datas) {
		for (String list : datas) {
			if (string.equals(list))
				return (true);
		}
		return false;
	}

}
