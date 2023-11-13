package basePackage;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.*;

public class BaseClass {

    protected APIRequestContext ctx;
    protected Playwright pw;

    @BeforeClass
    public void initializePlaywright(){
        pw = Playwright.create();
    }

    @BeforeMethod
    public void initializeAPIConnection(){
        APIRequest req = pw.request();
        ctx = req.newContext();
    }

    public APIRequestContext returnContext(){
        return ctx;
    }

    @AfterMethod
    public void tearDown(){
        ctx.dispose();
    }

    @AfterClass
    public void closePlaywright(){
        pw.close();
    }
}
