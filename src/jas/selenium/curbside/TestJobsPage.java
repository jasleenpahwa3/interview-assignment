package jas.selenium.curbside;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestJobsPage {

	private static WebDriver driver = new FirefoxDriver();

	//HashMap to store the location and number of jobs
	private static HashMap<String, Integer> locationMap = new HashMap<String, Integer>();

	public static void main(String[] args) {

		String url = "https://curbside.com/jobs/";
		openWebpage(url);

		//Read and display the job locations and their number of openings from the curbside  jobs page
		displayJobLocations();

		//Compare the curbside jobs page with the smart recruiters page
		validateSmartRecruiters();

		//close Firefox
		driver.close();

		// exit the program explicitly
		System.exit(0);
	}

	public static void openWebpage(String url) {

		String baseUrl = url;

		//Launch Firefox and direct it to the base URL
		driver.get(baseUrl);
		driver.manage().window().maximize();
	}

	public static void displayJobLocations() {
		//Get the jobs table 
		WebElement table = driver.findElement(By.id("jobsTable")); 

		//Get all the table row elements from the table 
		List<WebElement> rows = table.findElements(By.tagName("tr")); 

		// Iterate through all rows to read the column data
		for (WebElement rowElement : rows) {
			if (rows.indexOf(rowElement)==0){
				continue;
			}
			List<WebElement> cellElements = rowElement.findElements(By.xpath(".//td"));
			String jobLocation=cellElements.get(1).getText(); 
			if(locationMap.containsKey(jobLocation)){
				locationMap.replace(jobLocation, locationMap.get(jobLocation)+1);
			}
			else{
				locationMap.put(jobLocation,1);
			}
		}
		
		Iterator<String> iterator = locationMap.keySet().iterator();
		  
		while (iterator.hasNext()) {
		   String key = iterator.next();
		   String value = locationMap.get(key).toString();
		  
		   System.out.println(key + ":" + value);
		}
	}

	public static void validateSmartRecruiters(){

		String url = "https://careers.smartrecruiters.com/Curbside1/";
		openWebpage(url);

		List<WebElement> jobDetails = driver.findElements(By.cssSelector("ul[class='list--dotted title-list']"));

		HashMap<String, Integer> locationMapSmart = new HashMap<String, Integer>();
		for(int i=0; i<jobDetails.size(); i++){

			String[] jobLocation = jobDetails.get(i).getText().split("(?<=\\D)(?=\\d)");
			if(jobLocation[0].contains("Canada")){
				jobLocation[0]=jobLocation[0].replace("Canada","QC");
			}
			String[] numJobs = jobLocation[1].split(" ");
			locationMapSmart.put(jobLocation[0],Integer.parseInt(numJobs[0]));
		}
		
		if(locationMap.equals(locationMapSmart)){
			System.out.println("The two job pages are in sync!");
		}else{
			System.out.println("The two job pages are not in sync!Please check...");
		}

	}

}
