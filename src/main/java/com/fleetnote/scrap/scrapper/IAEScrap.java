package com.fleetnote.scrap.scrapper;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.fleetnote.scrap.models.entity.Contact;





public class IAEScrap {

	
	private static WebDriver mDriver;
	private static WebDriver mDriver2;
	private static ArrayList<String> urls;
	private static ArrayList<Contact> contacts;
	
	public static void main(String[] args) throws InterruptedException, IOException 
	{
		mDriver = new FirefoxDriver();
		contacts = new ArrayList<Contact>();
		
		IAEScrap iaes = new IAEScrap();
		urls= new ArrayList<String>();
		
		mDriver.get("http://www.iaeaixalumni.org");
	
		iaes.login(mDriver);

		Thread.sleep(10000);
		
		WebElement menu = mDriver.findElement(By.xpath("//*[@id='main_annuaires']"));
		menu.click();
		
		Thread.sleep(5000);
		
		mDriver.findElement(By.xpath("//*[@id='position_chzn']/a/span")).click();
		mDriver.findElement(By.xpath("//*[@id='position_chzn_o_10']")).click();
		
		WebElement liste = mDriver.findElement(By.xpath("//*[@id='search_list_view']"));
		liste.click();
		liste.click();
		Thread.sleep(5000);
		for(int i=0;i<20;i++)
		{
			
			for(int j=0;j<20;j++)
			{
				//Arret � 10
				for(int h=1;h<2;h++)
				{
					WebElement suivant = mDriver.findElement(By.xpath("//*[@id='paginate_grad_addressbook_paginator_top']/ul/li[2]/a"));
					suivant.click();
					Thread.sleep(4500);
				}
				List<WebElement> elements = mDriver.findElements(By.className("tromb"));
				Iterator<WebElement> it = elements.iterator();
				WebElement link = it.next();
				
				for(int k=0;k<=j;k++)
					link = it.next();
				
				WebElement linkprofile = link.findElement(By.tagName("a"));
				String url = linkprofile.getAttribute("href");
				urls.add(url);
				linkprofile.click();
				contacts.add(iaes.getContact(url));
				mDriver.navigate().back();
				
				Thread.sleep(5000);
			}
			WebElement suivant = mDriver.findElement(By.xpath("//*[@id='paginate_grad_addressbook_paginator_top']/ul/li[5]/a"));
			suivant.click();
			Thread.sleep(5000);
		}
		
		
	}



	public Contact getContact(String url) throws InterruptedException, IOException
	{
		Contact c = new Contact();
		Thread.sleep(3000);
		
		if(mDriver.findElements(By.xpath("//*[@id='user_information']/h1")).size() != 0 )
			c.setNom(mDriver.findElement(By.xpath("//*[@id='user_information']/h1")).getText());
		if(mDriver.findElements(By.xpath("//*[@id='user_information']/ul/li[2]")).size() != 0 )
			c.setFonction(mDriver.findElement(By.xpath("//*[@id='user_information']/ul/li[2]")).getText());
		
			if(mDriver.findElements(By.xpath("//*[@id='user_information']/ul/li[3]")).size() != 0 )
			{
				for(int i=1;i<5;i++)
				{
					if(mDriver.findElements(By.xpath("//*[@id='user_information']/ul/li[3]/dl/dd["+i+"]")).size()!= 0)
					{
						String texte = mDriver.findElement(By.xpath("//*[@id='user_information']/ul/li[3]/dl/dt["+i+"]")).getText();
						if(texte.contains("Adresse"))
							c.setAdresse(mDriver.findElement(By.xpath("//*[@id='user_information']/ul/li[3]/dl/dd["+i+"]")).getText().trim().replace("\n", " "));
						else if(texte.contains("Email"))
							c.setEmail(mDriver.findElement(By.xpath("//*[@id='user_information']/ul/li[3]/dl/dd["+i+"]")).getText());
						else if(texte.contains("T�l�phone"))
							c.setTelephonepro(mDriver.findElement(By.xpath("//*[@id='user_information']/ul/li[3]/dl/dd["+i+"]")).getText());
						
					}
				}
			}
			ecrire(c.getNom()+";"+c.getFonction()+";"+c.getAdresse()+";"+c.getTelephonepro()+";"+c.getEmail()+";"+"\n");
			return c;
	}
	
	
	
	public void login(WebDriver driver)
	{
		driver.get("http://www.iaeaixalumni.org");
		WebElement login = driver.findElement(By.xpath("//*[@id='username']"));
		WebElement pwd = driver.findElement(By.xpath("//*[@id='login_password']"));
		WebElement valid = driver.findElement(By.xpath("//*[@id='loginbutton']"));
		
		login.sendKeys("florent.mas@gmail.com");
		pwd.sendKeys("flo17flo");
		valid.click();
	}
	
	private static void ecrire(String texte) throws IOException
    {
       
        FileWriter writer = null;
        try{
             writer = new FileWriter("annuaire.csv", true);
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
