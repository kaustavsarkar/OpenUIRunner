
Google Search story

Narrative:
In order to test JBehave with Selenium
As a developer
I want to search google

Scenario: Why things do not work
Meta: @Run
Given Google is Open
When I type Search_String
Then I can see search options

Scenario: Why things do work
Meta: @Run
Given Google is Open
When I type Search_String
Then I can see search options
