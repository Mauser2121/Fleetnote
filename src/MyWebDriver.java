import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.IncorrectnessListener;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;

public class MyWebDriver extends HtmlUnitDriver
{
   

    public MyWebDriver ()
    {
        super(BrowserVersion.FIREFOX_3_6);
        setJavascriptEnabled(true);
        
//        getWebClient().setCssEnabled(false);
//        getWebClient().setThrowExceptionOnFailingStatusCode(false);
//        getWebClient().setThrowExceptionOnScriptError(false);

        getWebClient().setCssErrorHandler(new SilentCssErrorHandler());
        getWebClient().setIncorrectnessListener(new IncorrectnessListener()
        {
            public void notify(String message, Object source)
            {
            }
        });

      
    }
}