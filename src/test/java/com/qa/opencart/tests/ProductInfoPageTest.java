package com.qa.opencart.tests;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qa.opencart.base.BaseTest;

public class ProductInfoPageTest extends BaseTest{
	

	@BeforeClass
	public void productInfoSetup(){
		accPage=loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));
	}
	
	@Test
	public void productHeaderTest() {
		resultsPage=accPage.doSearch("macbook");
		productInfoPage=resultsPage.selectProduct("MacBook Pro");
		Assert.assertEquals(productInfoPage.getProductHeader(),"MacBook Pro");
		
	}
	@Test
	public void productInfoTest() {
		resultsPage=accPage.doSearch("macbook");
		productInfoPage=resultsPage.selectProduct("MacBook Pro");
		Map<String, String> actProducDataMap=productInfoPage.getProductData();
		
		
		softAssert.assertEquals(actProducDataMap.get("Brand"), "Apple");
		softAssert.assertEquals(actProducDataMap.get("Product Code"), "Product 18");
		softAssert.assertEquals(actProducDataMap.get("Reward Points"), "800");
		softAssert.assertEquals(actProducDataMap.get("Availability"), "Out Of Stock");
		softAssert.assertEquals(actProducDataMap.get("productprice"), "$2,000.00");
		softAssert.assertEquals(actProducDataMap.get("extaxprice"), "$2,000.00");
		softAssert.assertAll();
		//This line is mandatory for soft assert,this method will give the information how many assert methods got failed.
		//used for single test with multiple checks
	}
	
	@DataProvider
	public Object[][]  getProductImagesCountData() {
		return new Object[][] {
			{"macbook","MacBook Pro",4},
			{"imac","iMac",3},
			{"samsung","Samsung SyncMaster 941BW",1},
			{"samsung","Samsung Galaxy Tab 10.1",7},
			{"canon","Canon EOS 5D",3}
			};
	}
	
	@Test(dataProvider="getProductImagesCountData")
	public void productImagesCountTest(String searchKey,String productName,int imagesCount) {
		resultsPage=accPage.doSearch(searchKey);
		productInfoPage=resultsPage.selectProduct(productName);
		Assert.assertEquals(productInfoPage.getProductImageCount(),imagesCount) ;
	}
}
