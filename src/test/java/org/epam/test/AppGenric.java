package org.epam.test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

public class AppGenric {
	
	public Date giveRandomDate(int iMaxNumOfDays)
	{
		Date currentDate = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		Random rand = new Random();
		int numOfDays = rand.nextInt(iMaxNumOfDays)+1;
		c.add(Calendar.DAY_OF_MONTH, numOfDays);
		return c.getTime();
	}
	
	public void selectCalendarYearOrMonth(WebDriver driver,String year) throws Exception
	{   
		boolean blnStatus = false;
		List<WebElement> objList = null;
		while((!blnStatus))
		{
			objList = driver.findElements(By.xpath("//div[@class='calendar']//div[@class='pika-label']"));
			 for(WebElement ele :objList )
			 {
				 if(year.equalsIgnoreCase(ele.getText()))
				 {
					 blnStatus = true;
					 break;
				 }
			 }
			 if(!blnStatus)
			 {
				 WebElement btnNextMont = driver.findElement(By.xpath("//div[@class='calendar']//button[text()='Next month']"));
				 if(btnNextMont.isEnabled())
					 btnNextMont.click();
				 else
					 throw new Exception("Trying to select Invalid Flight calendar date ");
			 }
			 
		}
		 
	}
	
	public void selectCalendarDate(WebDriver driver,String month,String date)
	{
		int iCounter =0;
		date = (date.startsWith("0")?date.substring(1):date);
		List<WebElement> objList = driver.findElements(By.xpath("//div[@class='calendar']//div[@class='pika-label']"));
		 for(WebElement ele :objList )
		 {
			 ++iCounter;
			 if(month.equalsIgnoreCase(ele.getText()))
			 {
				 break;
			 }
		 }
		 String strDayXpath = String.format("(//div[@class='calendar']//div[@class='pika-label'])[%d]/ancestor::div[@class='pika-lendar']//td[@data-day='"+date+"']", iCounter);
		 driver.findElement(By.xpath(strDayXpath)).click();
	}

	
	public void waitForElementVisible(WebDriver driver,By byObject,int timeout)
	{
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(timeout, TimeUnit.SECONDS)
				.pollingEvery(5, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);
		
		Function<WebDriver,Boolean> function = new Function<WebDriver,Boolean>()
				{
					public Boolean apply(WebDriver t) {
						return driver.findElement(byObject).isDisplayed();
					}
				};
				wait.until(function);	
	}
	
	public boolean isTextPresent(WebDriver driver,String strText)
	{
			boolean blnStatus = false;
			
			String strXpath = "//*[text()[contains(.,'"+strText+"')]]";
			int iNumOfElements = driver.findElements(By.xpath(strXpath)).size();
			if(iNumOfElements>0)
				blnStatus = true;
			return blnStatus;
	}
	

}
