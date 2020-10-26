Google Search story

Narrative:
In order to tes JBehave with Selenium
As a developer
I want to search google

Scenario: Try searching in Google
Meta: @Run
Given Google is Open
When I type Search_String
And click on search
Then I can see search options

Scenario: Test searching in Google
Meta: @Run
Given Google is Open
When I type Search_String
And click on search
Then I can see search options
