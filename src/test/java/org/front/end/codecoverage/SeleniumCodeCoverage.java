package org.front.end.codecoverage;


import com.google.gson.Gson;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v115.profiler.Profiler;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;


public class SeleniumCodeCoverage {
    DevTools devTools;
    ChromeDriver driver;

    @BeforeMethod
    public void setup(){
        ChromeOptions chromeOptions = new ChromeOptions();
        // Launch the Chrome browser
        driver = new ChromeDriver(chromeOptions);
        // Get the DevTools instance
         devTools = driver.getDevTools();
        // Enable the Profiler domain
        // Create Session using Dev Tools Protocol
        devTools.createSession();
        devTools.send(Profiler.enable());
        // Start the Precise Coverage
        devTools.send(Profiler.startPreciseCoverage(Optional.of(true), Optional.of(true), Optional.of(false)));
        // Navigate to the page containing the JavaScript code you want to profile
        driver.navigate().to("https://facebook.com/home");
    }

    @AfterMethod
    public void teardown(){
        // Retrieve the coverage data
        Profiler.TakePreciseCoverageResponse coverageResponse = devTools.send(Profiler.takePreciseCoverage());
        // Process the coverage data
        Gson gson = new Gson();
        String coverageData = gson.toJson(coverageResponse).toString();
        System.out.println(coverageData);
        // Stop coverage collection
        devTools.send(Profiler.stopPreciseCoverage());
        // Close the browser
        driver.quit();
    }

    @Test
    public void loginTest(){
        driver.findElement(By.xpath("//input[@id='email']")).sendKeys("1234567890");
        driver.findElement(By.xpath("//input[@id='pass']")).sendKeys("tester123");
        driver.findElement(By.xpath("//button[@id='loginbutton']")).click();
    }

}
