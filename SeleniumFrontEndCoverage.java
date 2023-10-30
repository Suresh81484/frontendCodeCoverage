public class SeleniumFrontEndCoverage{

@Before
public void setup(){
                ChromeOptions chromeOptions = new ChromeOptions();
                // Launch the Chrome browser
                ChromeDriver driver = new ChromeDriver(chromeOptions);
                // Get the DevTools instance
                DevTools devTools = driver.getDevTools();
                // Enable the Profiler domain
                // Create Session using Dev Tools Protocol
                devTools.createSession();
                devTools.send(Profiler.enable());
                // Start the Precise Coverage
                devTools.send(Profiler.startPreciseCoverage(Optional.of(true), Optional.of(true),Optional.of(false)));
                // Navigate to the page containing the JavaScript code you want to profile
                driver.navigate().to("https://facebook.com/home");   
}

@After
public void teardown(){
                // Retrieve the coverage data
                Profiler.TakePreciseCoverageResponse coverageResponse = devTools.send(Profiler.takePreciseCoverage());
                // Process the coverage data
                // call the nodejs script
                // Stop coverage collection
                devTools.send(Profiler.stopPreciseCoverage());
                // Close the browser
                driver.quit();
}

@Test
public void userLogin(){


  //Functional Test Case
  Login the Facebook app


}

