package org.epam.test;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;



public class CheckFlightTest extends TestHelper {
	String expTitle = "Official Wizz Air website | Book direct for the cheapest prices";
	private By txtOrigin = By.id("search-departure-station");
	private By txtDestination = By.id("search-arrival-station");
	private By btnSearch = By.xpath("//button[@data-test='flight-search-submit' and text()='Search']");
	private String dynAirportSearchStart = "//div[starts-with(@class,'flight-search__panel__locations-container')]//*[text()[normalize-space(.)='";
	private String dynAirportSearch2End = "']]";
	private By Passengers = By.id("search-passenger");
	private By btnPassengersOK = By.xpath("//fieldset[contains(@class,'light-search__panel__fieldset--passengers')]//button[text()='OK']");
	private By btnChildAdd = By.xpath("//fieldset[contains(@class,'light-search__panel__fieldset--passengers')]//button[parent::div//*[text()[normalize-space(.)='child']] and contains(@class,'add')]");
	private By departureDateEle = By.id("search-departure-date");
	private By returnDateEle = By.id("search-return-date");
	private By btnCalendarOk =By.xpath("//div[@class='calendar']//button[text()='OK']");
	private By departureLabel = By.xpath("//div[@id='fare-selector-outbound']//label[contains(@class,'flight-select__chart__day__label--selected')]//time");
	private By returnLabel = By.xpath("//div[@id='fare-selector-return']//label[contains(@class,'flight-select__chart__day__label--selected')]//time");
	private By departureFlightcontent = By.xpath("//div[@id='fare-selector-outbound']//tbody[@class='booking-flow__prices-table__content']");
	 Date departureDate = null;
	 Date returnDate = null;
	@Test
	public void flightTest()
	{
		String strOrignCountry = "Belgium";
		String strOriginAirport = "Brussels Charleroi";
		String appTitle = driver.getTitle();
		Assert.assertEquals(expTitle, appTitle,"Title is not matching ");
		
		driver.findElement(txtOrigin).clear();
		driver.findElement(txtOrigin).sendKeys(strOrignCountry);	
		driver.findElement(By.xpath(dynAirportSearchStart +strOriginAirport+ dynAirportSearch2End)).click();
		String appOriginAirport = driver.findElement(txtOrigin).getAttribute("value");
		Assert.assertEquals(strOriginAirport, appOriginAirport,"Origin Airport value is not entered properly ");
		
		String strWrongDestinationCountry = "Germany";
		String strNoResultComments = "There is no result for this search.";
		String dynAirportSearchContainsStart = "//div[starts-with(@class,'flight-search__panel__locations-container')]//*[text()[contains(normalize-space(.),'";
		driver.findElement(txtDestination).clear();
		driver.findElement(txtDestination).sendKeys(strWrongDestinationCountry);
		boolean isDisplayed = driver.findElement(By.xpath(dynAirportSearchContainsStart +strNoResultComments+ "')]]")).isDisplayed();
		Assert.assertTrue(isDisplayed, "Empty Search search result is not displayed");
		

		String strDestinationAirport = "Budapest";
		String strDestinationCountry = "Hungary";		
		driver.findElement(txtDestination).clear();
		driver.findElement(txtDestination).sendKeys(strDestinationCountry);
		driver.findElement(By.xpath(dynAirportSearchStart +strDestinationAirport+ dynAirportSearch2End)).click();
		String appDestinationAirport = driver.findElement(txtDestination).getAttribute("value");
		Assert.assertEquals(strDestinationAirport, appDestinationAirport,"Destination Airport value is not entered properly ");
		
	}
	
	@Test(dependsOnMethods={"flightTest"})
	public void selectDepartureAndReturnDate() throws Exception
	{
		int iMaxRandomDays=180,iNumOfDaysOfTrip=5;
		 departureDate = objAppGeneric.giveRandomDate(iMaxRandomDays);
		 SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-YYYY");
		 String strDepartureDate = sdf.format(departureDate);
		 System.out.println("Departure Date:"+strDepartureDate);
		 String[] arrDepartureDate = strDepartureDate.split("-");
		 driver.findElement(departureDateEle).click();
		 objAppGeneric.waitForElementVisible(driver, By.xpath("//div[@class='calendar']//div[@class='pika-label']"), 20);
		 objAppGeneric.selectCalendarYearOrMonth(driver,arrDepartureDate[2]);
		 objAppGeneric.selectCalendarYearOrMonth(driver,arrDepartureDate[1]);
		 objAppGeneric.selectCalendarDate(driver,arrDepartureDate[1],arrDepartureDate[0]);
		 driver.findElement(btnCalendarOk).click();
		 
		 
		 Calendar cal = Calendar.getInstance();
		 cal.setTime(departureDate);		 		 		 
		 cal.add(Calendar.DAY_OF_MONTH, iNumOfDaysOfTrip);
		 returnDate = cal.getTime();
		 String strReturnDate = sdf.format(returnDate);
		 System.out.println("return Date:"+strReturnDate);
		 String[] arrReturnDate = strReturnDate.split("-");
		 driver.findElement(returnDateEle).click();
		 objAppGeneric.waitForElementVisible(driver, By.xpath("//div[@class='calendar']//div[@class='pika-label']"), 20);
		 objAppGeneric.selectCalendarYearOrMonth(driver,arrReturnDate[2]);
		 objAppGeneric.selectCalendarYearOrMonth(driver,arrReturnDate[1]);
		 objAppGeneric.selectCalendarDate(driver,arrReturnDate[1],arrReturnDate[0]);
		 driver.findElement(btnCalendarOk).click();
	}
	
	@Test(dependsOnMethods={"selectDepartureAndReturnDate"})
	public void selectPassengers()
	{
		String expPasengers = "1 adult 1 child";
		objAppGeneric.waitForElementVisible(driver, Passengers, 30);
		driver.findElement(Passengers).click();
		objAppGeneric.waitForElementVisible(driver, btnChildAdd, 30);
		driver.findElement(btnChildAdd).click();
		objAppGeneric.waitForElementVisible(driver, btnPassengersOK, 30);
		driver.findElement(btnPassengersOK).click();
		String appPassengers = driver.findElement(Passengers).getText().trim();
		Assert.assertEquals(appPassengers, expPasengers,"Passengers data is not filled properly");
	}
	
	@Test(dependsOnMethods={"selectPassengers"})
	public void validateSearchResults()
	{
		driver.findElement(btnSearch).click();		
		objAppGeneric.waitForElementVisible(driver, departureLabel, 30);
		boolean filghtResultsIsPresent = objAppGeneric.isTextPresent(driver, "Select flights");
		Assert.assertTrue(filghtResultsIsPresent,"Flight Results Page is not prsent");
	
		String appDepartureDate = driver.findElement(departureLabel).getText().trim().toLowerCase();
		String appReturnDate = driver.findElement(returnLabel).getText().trim().toLowerCase();
		SimpleDateFormat sdf = new SimpleDateFormat("EEE dd, MMM");
		String strReturnDate = sdf.format(returnDate);
		Assert.assertEquals(strReturnDate.toLowerCase(), appReturnDate,"Return date is not entered properly");
		String strDepartureDate = sdf.format(departureDate);
		Assert.assertEquals(strDepartureDate.toLowerCase(), appDepartureDate,"Departure date is not entered properly");
		
		int NumofDepartureFlights = driver.findElements(departureFlightcontent).size();
		System.out.println("Num Of Departure Flights:"+NumofDepartureFlights);
		for(int i=1;i<=NumofDepartureFlights;i++)
		{
			System.out.print("Departure Flight"+i+" :");
			String strdepartureFlightDetails = String.format("(//div[@id='fare-selector-outbound']//tbody[@class='booking-flow__prices-table__content']//td[contains(@class,'prices-table__content__column--time')])[%d]", i);
			System.out.println(driver.findElement(By.xpath(strdepartureFlightDetails)).getText().trim());
		}
		
	}
	
}
