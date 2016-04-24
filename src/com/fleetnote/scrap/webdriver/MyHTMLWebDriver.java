package com.fleetnote.scrap.webdriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.IncorrectnessListener;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;


public class MyHTMLWebDriver extends HtmlUnitDriver {


	    public MyHTMLWebDriver ()
	    {
	        super(BrowserVersion.CHROME_16);
	        setJavascriptEnabled(true);
	       
	       
	        getWebClient().setCssErrorHandler(new SilentCssErrorHandler());
	        getWebClient().setIncorrectnessListener(new IncorrectnessListener()
	        {
	            public void notify(String message, Object source)
	            {
	            }
	        });

	        getWebClient().setAlertHandler(new AlertHandler()
	        {
	            public void handleAlert(Page page, String message)
	            {
	            }
	        });
	    }
}
