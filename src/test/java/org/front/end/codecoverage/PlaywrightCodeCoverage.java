package org.front.end.codecoverage;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.CDPSession;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.openqa.selenium.By;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PlaywrightCodeCoverage {

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    private CDPSession cdpSession;

    @BeforeMethod
    public void setup(){
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setDevtools(false).setHeadless(false));
        context = browser.newContext();
        page = context.newPage();
        page.navigate("https://facebook.com/");
        //This line creates a new Chrome DevTools Protocol (CDP) session associated with the current page. CDP provides advanced debugging and profiling            capabilities for Chromium-based browsers
        cdpSession = page.context().newCDPSession(page);
        //Here, a JSON object named params is created to specify configuration options for the Profiler.
        JsonObject params = new JsonObject();
        //This sets the "callCount" property in the params object to true, which likely indicates a desire to count function calls in the profiling data.
        params.addProperty("callCount", true);
        //This sets the "detailed" property in params to true, indicating a preference for collecting detailed profiling information.
        params.addProperty("detailed", true);
        //The "allowTriggeredUpdates" property is set to false in params, suggesting a preference to disable triggered updates during profiling.
        params.addProperty("allowTriggeredUpdates", false);
        cdpSession = page.context().newCDPSession(page);
        cdpSession.send("Profiler.enable", (JsonObject)null);
        cdpSession.send("Profiler.start", (JsonObject)null);
        cdpSession.send("Profiler.startPreciseCoverage", params);
    }

    @AfterMethod
    public void teardown(){
        JsonObject coverage = cdpSession.send("Profiler.takePreciseCoverage", (JsonObject)null);
        Gson gson = new Gson();
        String coverageData = gson.toJson(coverage).toString();
        System.out.println(coverageData);
        page.close();
    }

    @Test
    public void loginTest(){
        page.fill("#email","1234567890");
        page.fill("#pass","tester123");
        page.click("//button[@name='login']");
    }
}
