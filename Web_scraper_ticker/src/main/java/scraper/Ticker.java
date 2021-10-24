package scraper;

import com.opencsv.CSVWriter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.support.ui.LoadableComponent;

public class Ticker {

    @FindBy(xpath = "//tr//td[1]//a")
    private List<WebElement> stock_ticker;
    @FindBy(xpath = "//select[contains(@name,'perpage')]")
    private WebElement perpage;
    @FindBy(xpath = "//option[contains(@value,'10000')]")
    private WebElement select_10k;
    @FindBy(xpath = "//tr//td[2]")
    private  List<WebElement> company_name;

    @Test
    public void ticker_stock() {
        System.setProperty("webdriver.chrome.driver", "D:\\Finacial Topology\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
//        PageFactory.initElements(driver, this);
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        String URL = "https://stockanalysis.com/stocks/";
        driver.get(URL);
        driver.findElement(By.xpath("//select[contains(@name,'perpage')]")).click();
//        perpage.click();
        driver.findElement(By.xpath("//option[contains(@value,'10000')]")).click();
//        select_10k.click();
        List<WebElement> list = driver.findElements(By.xpath("//tr//td[1]//a"));
        String t,cn;
        List<String[]> data = new ArrayList<String[]>();
        for (int i=1;i<=list.size();i++){
            t = driver.findElement(By.xpath("(//tr//td[1]//a)["+i+"]")).getText();
            cn = driver.findElement(By.xpath("(//tr//td[2])["+i+"]")).getText();
            System.out.println(t+" "+cn);
            data.add(new String[]{t,cn});
            writeToCsvAtOnce("D:\\Finacial Topology\\USA_stockTicker.csv",data);
        }
//        String t,cn;
//        for (int i = 0; i < stock_ticker.size(); ++i){
//            t = stock_ticker.get(i).getText();
//            cn = company_name.get(i).getText();
//            System.out.println(t+" "+cn);
//            data.add(new String[]{t,cn});
//            writeToCsvAtOnce("D:\\Finacial Topology\\USA_stockTicker.csv",data);
//        }
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
