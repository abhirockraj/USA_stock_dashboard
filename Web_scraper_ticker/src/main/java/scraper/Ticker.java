package scraper;

import com.opencsv.CSVWriter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.FindBy;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Ticker {
    @FindBy(xpath = "//tr//td[1]//a")
    private List<WebElement> stock_ticker;
    @FindBy(xpath = "//select[contains(@name,'perpage')]")
    private WebElement perpage;
    @FindBy(xpath = "//option[contains(@value,'10000')]")
    private WebElement select_10k;

    @Test
    public void ticker_stock() {
        System.setProperty("webdriver.chrome.driver", "D:\\Finacial Topology\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        String URL = "https://stockanalysis.com/stocks/";
        driver.get(URL);
        driver.findElement(By.xpath("//select[contains(@name,'perpage')]")).click();
        driver.findElement(By.xpath("//option[contains(@value,'10000')]")).click();
        List<WebElement> list = driver.findElements(By.xpath("//tr//td[1]//a"));
        String ticker = null;
        List<String[]> data = new ArrayList<String[]>();
        for (int i=1;i<=list.size();i++){
            ticker = driver.findElement(By.xpath("(//tr//td[1]//a)["+i+"]")).getText();
            data.add(new String[]{ticker});
            writeToCsvAtOnce("D:\\Finacial Topology\\USA_stockTicker.csv",data);
        }
    driver.close();
    }
    public  void  writeToCsvAtOnce(String filepath, List<String[]> data){
        File file = new  File(filepath);
        try{
            FileWriter output = new FileWriter(file);
            CSVWriter writer =new CSVWriter(output);
            writer.writeAll(data);
            writer.close();

        }
        catch (Exception e){
            System.out.println(e);
        }
    }

}
