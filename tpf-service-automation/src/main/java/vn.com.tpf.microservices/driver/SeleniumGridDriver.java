package vn.com.tpf.microservices.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class SeleniumGridDriver {
    private WebDriver driver;
    private String browser;
    private String baseUrl;
    private String os;
    private String hub;

    public SeleniumGridDriver(String os, String browser, String baseUrl, String hub,String seleHost,String selePort) throws MalformedURLException {
    	// String host = System.getProperty("seleniumHubHost"); // TODO: uncomment this line if deploy on docker
    	//String host = "10.10.10.10";
        //String host = "localhost";
        //String host="tpf-opensource-selenium-hub";
        //String host = "10.1.64.41";

    	this.browser = browser;
        this.os = os;
        this.baseUrl = baseUrl;
        this.hub = hub;

//        Platform platform = Platform.fromString(os.toUpperCase());
        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--no-sandbox");
            chromeOptions.addArguments("headless");
            chromeOptions.addArguments("--incognito");
            chromeOptions.addArguments("--disable-gpu");
            chromeOptions.addArguments("--disable-dev-shm-usage");
//            chromeOptions.addArguments("start-maximized");
            chromeOptions.addArguments("window-size=2560x3000");
            //chromeOptions.addArguments("--window-size=2560,1080");
//            chromeOptions.setCapability("platform", platform);
            this.driver = new RemoteWebDriver(new URL("http://" + seleHost + ":"+ selePort + "/wd/hub"), chromeOptions);
        } else if (browser.equalsIgnoreCase("firefox")) {
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            this.driver = new RemoteWebDriver(new URL("http://" + seleHost + ":"+ selePort + "/wd/hub"), firefoxOptions);
        } else {
            InternetExplorerOptions ieOption = new InternetExplorerOptions();
            this.driver = new RemoteWebDriver(new URL("http://" + seleHost + ":"+ selePort + "/wd/hub"), ieOption);
        }

        this.driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

        this.driver.get(baseUrl);
    }

    public String getOs() {
        return this.os;
    }

    public String getBrowser() {
        return this.browser;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public String getHub() {
        return this.hub;
    }

    public WebDriver getDriver() {
        return this.driver;
    }
}
