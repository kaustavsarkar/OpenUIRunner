package org.our.example.pages.google;

import org.openqa.selenium.By;

public class SearchGooglePage {

    private static final By SEARCH_BOX = By.name("q");
    private static final By SEARCH_BUTTON = By.name("btnK");
    private static final By ANCHOR_TAGS = By.tagName("a");

    public By getSearchBox() {
        return (SEARCH_BOX);
    }

    public By getSearchButton() {
        return (SEARCH_BUTTON);
    }

    public By getSearchOptions() {
        return (ANCHOR_TAGS);
    }
}
