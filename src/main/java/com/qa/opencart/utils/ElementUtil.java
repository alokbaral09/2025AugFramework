package com.qa.opencart.utils;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qa.opencart.exceptions.FrameWorkException;
import com.qa.opencart.factory.DriverFactory;

public class ElementUtil {
	private WebDriver driver;
	private Actions action;
	private JavaScriptUtil jsUtil;

	public ElementUtil(WebDriver driver) {
		this.driver = driver;
		action = new Actions(driver);
		jsUtil= new JavaScriptUtil(driver);
	}

	public void doClick(By locator) {
		getElement(locator).click();
	}

	public void doClick(By locator, int timeout) {
		waitForElementVisible(locator, timeout).click();
	}

	public void doSendKeys(By locator, String value) {
		getElement(locator).sendKeys(value);
	}
	
	public void doSendKeys(WebElement element, String value) {
	  element.clear();
	  element.sendKeys(value);
	}

	public void doSendKeys(By locator, String value, int timeout) {
		waitForElementVisible(locator, timeout).sendKeys(value);
	}

//CharSequence is an interface implemented by String,StringBuilder,StringBuffer
	public void doSendKeys(By locator, CharSequence... value) {
		getElement(locator).sendKeys(value);
	}

   private void checkElementHighlight(WebElement element) {
	   if(Boolean.parseBoolean(DriverFactory.isHighlight)) {
			jsUtil.flash(element);
		}
   }
	
	public WebElement getElement(By locator) {
		
		WebElement element=driver.findElement(locator);
		//checkElementHighlight(element);
		return element;

	}

	public boolean isElementDisplayed(By locator) {
		try {
			return getElement(locator).isDisplayed();
		} catch (NoSuchElementException e) {
			System.out.println("Element is not diplayed:" + locator);
			return false;
		}
	}

	public String doGetElementText(By locator) {

		String eleText = getElement(locator).getText();
		if (eleText != null) {
			return eleText;
		} else {
			System.out.println("Element text is null:" + eleText);
			return null;
		}

	}

	public String doElementGetAttribute(By locator, String attrName) {
		return getElement(locator).getAttribute(attrName);

	}

	public List<WebElement> getElements(By locator) {

		return driver.findElements(locator);

	}

	public int getElementsCount(By locator) {
		return getElements(locator).size();
	}

	public boolean isElementPresent(By locator, int expectedElementCount) {
		if (getElementsCount(locator) == expectedElementCount) {
			return true;
		}
		return false;

	}

	public boolean isElementNotPresent(By locator) {
		if (getElementsCount(locator) == 0) {
			return true;
		}
		return false;

	}

	public boolean isElementPresentMultipleTimes(By locator) {
		if (getElementsCount(locator) >= 1) {
			return true;
		}
		return false;

	}

	public boolean isElementPresent(By locator) {
		if (getElementsCount(locator) == 1) {
			return true;
		}
		return false;

	}

	public void printElementTextList(By locator) {
		List<String> eleTextList = getElementsTextList(locator);
		for (String e : eleTextList) {
			System.out.println(e);
		}
	}

	public List<String> getElementsTextList(By locator) {
		List<WebElement> eleList = getElements(locator);
		List<String> eleTextList = new ArrayList<String>();

		for (WebElement e : eleList) {
			String eleText = e.getText();
			if (eleText.length() != 0) {
				eleTextList.add(eleText);
			}
		}
		return eleTextList;
	}

	public boolean doSearch(By searchField, By suggestions, String SearchKey, String matchValue)
			throws InterruptedException {
		boolean flag = false;
		// driver.findElement(searchField).sendKeys(SearchKey);
		doSendKeys(searchField, SearchKey);
		Thread.sleep(3000);

		// List<WebElement> suggList = driver.findElements(suggestions);
		List<WebElement> suggList = getElements(suggestions);
		int totalSuggestions = suggList.size();
		System.out.println("Total number of suggestions==" + totalSuggestions);
		if (totalSuggestions == 0) {
			System.out.println("No suggestions found...");
			throw new FrameWorkException("No Suggestion Found");
		}
		for (WebElement e : suggList) {
			String text = e.getText();
			System.out.println(text);
			if (text.contains(matchValue)) {
				e.click();
				flag = true;
				break;// if you don't want to go further suggestion once training is found
			}
		}
		if (flag) {
			System.out.println(matchValue + " is found");
			return true;
		} else {
			System.out.println(matchValue + " is not found");
			return false;
		}
	}

	// ************Dropdwon Utils***************//

	private Select getSelect(By locator) {
		return new Select(getElement(locator));
	}

	public int getDropDownoptionsCount(By locator) {
		// Select select = new Select(getElement(locator));
		// return select.getOptions().size();

		return getSelect(locator).getOptions().size();

	}

	public void selectDropDownValueByVisibleText(By locator, String visibleText) {
		// Select select = new Select(getElement(locator));
		// select.selectByVisibleText(visibleText);

		getSelect(locator).selectByVisibleText(visibleText);
	}

	public void selectDropDownValueByIndex(By locator, int index) {
		// Select select = new Select(getElement(locator));
		// select.selectByIndex(index);
		getSelect(locator).selectByIndex(index);
	}

	public void selectDropDownValueByValue(By locator, String value) {
		// Select select = new Select(getElement(locator));
		// select.selectByValue(value);
		getSelect(locator).selectByValue(value);

	}

	public List<String> getDropDownOptionsTextList(By locator) {
		// Select select = new Select(getElement(locator));
		List<WebElement> optionsList = getSelect(locator).getOptions();
		System.out.println("Total number of options:" + optionsList.size());
		List<String> optionsTextList = new ArrayList<String>();
		for (WebElement e : optionsList) {
			String text = e.getText();

			optionsTextList.add(text);
		}
		return optionsTextList;
	}

	public void selectDropDownValueUsingSelect(By locator, String value) {
		// Select select = new Select(getElement(locator));
		List<WebElement> optionsList = getSelect(locator).getOptions();
		System.out.println("Total number of options:" + optionsList.size());

		for (WebElement e : optionsList) {
			String text = e.getText();

			if (text.equals(value)) {
				e.click();
				break;
			}
		}
		// selectDropDown(optionsList , value);
	}

	// select drop down value without using select class
	public void selectDropDownValue(By locator, String value) {
		List<WebElement> optionsList = getElements(locator);
		/*
		 * System.out.println("Total number of options:" + optionsList.size()); for
		 * (WebElement e : optionsList) { String text = e.getText();
		 * System.out.println(text);
		 * 
		 * if (text.equals(value)) { e.click(); break; }
		 * 
		 * }
		 */
		selectDropDown(optionsList, value);
	}

	private void selectDropDown(List<WebElement> optionsList, String value) {
		System.out.println("Total number of options:" + optionsList.size());
		for (WebElement e : optionsList) {
			String text = e.getText();
			System.out.println(text);

			if (text.equals(value)) {
				e.click();
				break;
			}

		}
	}

	// ******************Actions Utils****************

	public void doActionClick(By locator) {
		// Actions action= new Actions(driver);
		action.click(getElement(locator)).perform();

	}

	public void doActionSendKeys(By locator, String value) {
		// Actions action= new Actions(driver);
		action.sendKeys(getElement(locator), value).perform();

	}

	public void doActionsSendkeysWithPause(By locator, String value, long pauseTime) {

		// Actions action = new Actions(driver);
		char ch[] = value.toCharArray();
		for (char c : ch) {
			action.sendKeys(getElement(locator), String.valueOf(c)).pause(pauseTime).perform();

		}

	}

	/**
	 * This method is handing two level of parent and child menu on basis of By
	 * locator
	 * 
	 * @param parentMenu
	 * @param childMenu
	 * @throws InterruptedException
	 */
	public void parentChildMenu(By parentMenu, By childMenu) throws InterruptedException {

		// Actions action = new Actions(driver); we have initialize on the constructor
		// so no need to create again
		action.moveToElement(getElement(parentMenu)).perform();

		Thread.sleep(3000);
		// getElement(childMenu).click();
		doClick(childMenu);

	}

	public void parentChildMenu(String parentMenu, String childMenu) throws InterruptedException {

		// Actions action = new Actions(driver);
		action.moveToElement(getElement(By.xpath("//*[text()='" + parentMenu + "']"))).perform();

		Thread.sleep(3000);
		// getElement(By.xpath("//*[text()='" + childMenu + "']")).click();
		doClick(By.xpath("//*[text()='" + childMenu + "']"));

	}

	/**
	 * This method is handing four level of parent and child menu on basis of By
	 * locator
	 * 
	 * @param parentMenu
	 * @param childMenu
	 * @throws InterruptedException
	 */
	public void parentChildMenu(By level1, By level2, By level3, By level4) throws InterruptedException {

		// getElement(level1).click();
		doClick(level1);
		Thread.sleep(1000);
		// Actions action = new Actions(driver);
		action.moveToElement(getElement(level2)).perform();
		Thread.sleep(1000);
		action.moveToElement(getElement(level3)).perform();
		Thread.sleep(1000);
		// getElement(level4).click();
		doClick(level4);

	}

	// *****************Wait Utils***********//

	/**
	 * * An expectation for checking that an element is present on the DOM of a
	 * page. This does not necessarily mean that the element is visible on the page.
	 * 
	 * @param locator
	 * @param timeout
	 * @return
	 */

	public WebElement waitForElementPresent(By locator, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		 WebElement element=wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		// checkElementHighlight(element);
		 return element;
	}

	/**
	 * An expectation for checking that an element is present on the DOM of a page
	 * and visible on the page as well. Visibility means that the element is not
	 * only displayed but also has a height and width that is greater than 0.
	 * Default poling time/interval time=500ms
	 * 
	 * @param locator
	 * @param timeout
	 * @return
	 */
	public WebElement waitForElementVisible(By locator, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		WebElement element= wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		//checkElementHighlight(element);
		 return element;
	}

	public WebElement waitForElementVisible(By locator, int timeout, int intervalTimeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout),
				Duration.ofSeconds(intervalTimeout));
		WebElement element= wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		//checkElementHighlight(element);
		 return element;
	}

	/**
	 * wait for element visible os page with fluent wait features
	 * @param locator
	 * @param timeout
	 * @param pollingTime
	 * @return
	 */
	public WebElement waitForElementVisibleWithFluentFeatures(By locator, int timeout,int pollingTime) {
		Wait<WebDriver> wait= new FluentWait<WebDriver>(driver)
				              .withTimeout(Duration.ofSeconds(timeout))
				              .pollingEvery(Duration.ofSeconds(pollingTime))
				              .ignoring(NoSuchElementException.class)
				 		      .ignoring(StaleElementReferenceException.class)
				 		      .ignoring(ElementNotInteractableException.class)
				 		      .withMessage("==element is not found=="+locator);
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
	/**
	 * An expectation for checking an element is visible and enabled such that you
	 * can click it.
	 */
	public void waitForElementAndClick(By locator, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		wait.until(ExpectedConditions.elementToBeClickable(locator)).click();

	}

	/**
	 * An expectation for checking that all elements present on the web page that
	 * match the locator are visible. Visibility means that the elements are not
	 * only displayed but also have a height and width that is greater than 0.
	 * 
	 * @param locator
	 * @param timeout
	 * @return
	 */
	public List<WebElement> waitForElementsVisible(By locator, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
	}

	/**
	 * An expectation for checking that there is at least one element present on a
	 * web page.
	 * 
	 * @param locator
	 * @param timeout
	 * @return
	 */
	public List<WebElement> waitForElementsPresence(By locator, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
	}

	public String getPageTitleIs(String expectedTitle, int timeout) {
		if (waitForTitleIs(expectedTitle, timeout)) {
			return driver.getTitle();
		} else {
			return "-1";
		}

	}

	public String getPageTitleContains(String expectedTitle, int timeout) {
		if (waitForTitleContains(expectedTitle, timeout)) {
			return driver.getTitle();
		} else {
			return "-1";
		}

	}

	public boolean waitForTitleIs(String expectedTitle, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));

		try {
			return wait.until(ExpectedConditions.titleIs(expectedTitle));// if title not matched it will throw Timeout
																			// exception
		} catch (TimeoutException e) {
			System.out.println("Title is not matched");
			return false;
		}
	}

	public boolean waitForTitleContains(String fractionTitle, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));

		try {
			return wait.until(ExpectedConditions.titleContains(fractionTitle));
		} catch (TimeoutException e) {
			System.out.println("Title is not matched");
			return false;
		}
	}

	public String waitForTitleContainsAndReturn(String fractionTitle, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));

		try {
			wait.until(ExpectedConditions.titleContains(fractionTitle));
			return driver.getTitle();
		} catch (TimeoutException e) {
			System.out.println("Title is not matched");
			return "-1";
		}
	}

	public String getPageURLContains(String fractionURL, int timeout) {
		if (waitForURLContains(fractionURL, timeout)) {
			return driver.getCurrentUrl();
		} else {
			return "-1";
		}

	}

	public boolean waitForURLContains(String fractionURL, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));

		try {
			return wait.until(ExpectedConditions.urlContains(fractionURL));
		} catch (TimeoutException e) {
			System.out.println("URL is not matched");
			return false;
		}
	}

	public String waitForURLContainsAndReturn(String fractionURL, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));

		try {
			wait.until(ExpectedConditions.urlContains(fractionURL));
			return driver.getCurrentUrl();
		} catch (TimeoutException e) {
			System.out.println("URL is not matched");
			return "-1";
		}
	}

	public Alert waitForAlertAndSwitch(int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		return wait.until(ExpectedConditions.alertIsPresent());
	}

	
	public Alert waitForAlertUsingFluentWaitAndSwitch(int timeout) {
		Wait<WebDriver> wait= new FluentWait<WebDriver>(driver)
                              .withTimeout(Duration.ofSeconds(timeout))
                              .ignoring(NoAlertPresentException.class)
                              .withMessage("====js alert is not present");
		return wait.until(ExpectedConditions.alertIsPresent());
	}
	
	public String getAlertText(int timeout) {

		return waitForAlertAndSwitch(timeout).getText();
	}

	public void acceptAlert(int timeout) {
		waitForAlertAndSwitch(timeout).accept();
	}

	public void dismissAlert(int timeout) {
		waitForAlertAndSwitch(timeout).dismiss();
	}

	public void enterValueOnAlert(int timeout, String value) {
		waitForAlertAndSwitch(timeout).sendKeys(value);
	}
	
	
	//Wait for frame:
	public void waitForFrameUsingLocatorAndSwitchToIt(By frameLocator,int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
	}
	
	public void waitForFrameUsingLocatorAndSwitchToIt(int frameIndex,int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameIndex));
	}
	public void waitForFrameUsingLocatorAndSwitchToIt(String idOrName,int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(idOrName));
	}
	
	public void waitForFrameUsingLocatorAndSwitchToIt(WebElement frameElement,int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameElement));
	}
	
	//wait for Windows:
	public boolean waitForNewWindowOrTab(int expectedNumberOfWindows,int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		try {
			if(wait.until(ExpectedConditions.numberOfWindowsToBe(expectedNumberOfWindows)))
				return true;
		}catch(TimeoutException e) {
			System.out.println("number of windows are not matched......");
		}
		return false;
		
	}
	
}
