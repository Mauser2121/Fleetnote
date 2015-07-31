package webdriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.testng.log4testng.Logger;

import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.IncorrectnessListener;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;


public class MyWebDriver extends HtmlUnitDriver {

	 private static final Logger logger = Logger.getLogger(MyWebDriver.class);

	    public MyWebDriver ()
	    {
	        super(BrowserVersion.FIREFOX_17);
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
