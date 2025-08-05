package com.qa.opencart.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class JavaScriptUtil {
	private WebDriver driver;
	private JavascriptExecutor js;

	public JavaScriptUtil(WebDriver driver) {
		this.driver = driver;
		js = (JavascriptExecutor) driver;

	}

	public String getPageTitle() {
		// JavascriptExecutor js = (JavascriptExecutor) driver;
		return js.executeScript("return document.title;").toString();

	}

	public String getPageURL() {
		// JavascriptExecutor js= (JavascriptExecutor)driver;
		return js.executeScript("return document.URL;").toString();
	}

	public void generateJsAlert(String mesg) {
		// js.executeScript("alert('Hello')");
		js.executeScript("alert('" + mesg + "')");
	}

	public String getPageInnerText() {
		return js.executeScript("return document.documentElement.innerText;").toString();
	}

	// history.go(0)-refresh the page
	// history.go(-1)-go backward
	// history.go(+1)-go forward

	public void goBackWithJS() {
		js.executeScript("history.go(-1)");

	}

	public void goForwardWithJS() {
		js.executeScript("history.go(+1)");

	}

	public void pageRefeshWithJS() {
		js.executeScript("history.go(0)");

	}

	public void zoomChromeEdgeSafariFirefox(String zoomPercentage) {
		String zoom = "document.vody.style.zoom='" + zoomPercentage + "'";
		js.executeScript(zoom);
	}

	public void zoomFirefox(String zoomPercentage) {
		String zoom = "document.vody.style.MozTransForm='scaale(" + zoomPercentage + ")'";
		js.executeScript(zoom);
	}

	public void scrollMiddlePaageDown() {
		js.executeScript("window.scrollTo(0,document.body.scrollHeight/2);");
	}

	public void scrollPaageDown() {
		js.executeScript("window.scrollTo(0,document.body.scrollHeight);");
	}

	public void scrollPaageDown(String height) {
		js.executeScript("window.scrollTo(0,'" + height + "');");
	}

	public void scrollPaageUp() {
		js.executeScript("window.scrollTo(document.body.scrollHeight,0);");
	}

	public void scrollIntoView(WebElement element) {
		js.executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public void drawBorder(WebElement element) {
		js.executeScript("arguments[0].style.border='3px solid red'", element);
	}

	public void flash(WebElement element) {
		String bgcolor = element.getCssValue("backgroundColor");// white
		for (int i = 0; i < 100; i++) {
			changeColor("rgb(0,200,0)", element);// Green
			changeColor(bgcolor, element);// White

		}
	}

	private void changeColor(String color, WebElement element) {
		js.executeScript("arguments[0].style.backkgroundColor= '" + color + "'", element);
		/*
		 * try { Thread.sleep(1000); } catch (InterruptedException e) {
		 * e.printStackTrace(); }
		 */

	}
}
