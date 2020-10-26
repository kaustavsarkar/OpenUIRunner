package actions.google;

import actions.our.StoryContext;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import page.google.SearchGooglePage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SearchGoogleAction extends Steps{
	private static final String CLASSNAME = SearchGoogleAction.class.getSimpleName();
	private WebDriver driver;
	public SearchGoogleAction(WebDriver driver) {
		System.out.println(CLASSNAME+": Inside constructor");
		System.out.println(CLASSNAME+": Running Thread "+Thread.currentThread().getName());
		this.driver = driver;
	}

	@Given("Google is Open")
	public void checkGoogleIsOpen() {
		String title = driver.getTitle();
		System.out.println(CLASSNAME+": Check Title "+title);
		assertEquals("Google",title);
	}
	
	@When("I type $searchString")
	public void typeSearchString(String searchString) {
		
		String search = StoryContext.getData(searchString);

		System.out.println(CLASSNAME+": Story : "+StoryContext.getStory());
		
		System.out.println(CLASSNAME+": Search String "+search);
		
		SearchGooglePage page = new SearchGooglePage();
		WebElement box = driver.findElement(page.getSearchBox());
		
		System.out.println(box);
				box.sendKeys(search);
				box.sendKeys(Keys.TAB);
				box.sendKeys(Keys.TAB);
				box.sendKeys(Keys.ENTER);
		
	}
	
	@When("click on search")
	public void clickOnSearch() {
		SearchGooglePage page = new SearchGooglePage();
		//driver.findElement(page.getSearchButton()).click();
	}
	
	@Then("I can see search options")
	public void checkSearchOptions() {
		SearchGooglePage page = new SearchGooglePage();
		
		assertTrue(!driver.findElements(page.getSearchOptions()).isEmpty());
	}
	
}
