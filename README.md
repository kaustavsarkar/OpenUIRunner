# OpenUIRunner

---
 ***This projects aims to minimise development time for performing Behaviour 
 Driven Development.
 It is a minimal config set-up for running [Selenium](https://www.selenium.dev/) with [JBehave](https://jbehave.org/).***

## Motivation

---
The motivation behind creating this framework is to bring the testing 
efforts in sync with the Agile methodologies. An Agile way of SDLC demands 
the creation of [User Stories](https://en.wikipedia.org/wiki/User_story).

Developers and testers working on the User Stories shall write code, unit 
tests, integration tests, scripts for manual tests. However, there should be 
a way to map the User Stories against the tests themselves.

Usually organisations create their custom versions of this tool, however, 
this requires a lot of configuration to be done at the part of the tester 
and testers are not always proficient with developmental practices. This 
arises problems like:
- Lack of Standards: Tools created once are very organisation specific. 
  Hence, the same tool may require a lot of rework to make it work in 
  another. Also, every organisation may have different standards and 
  practices being followed.
- Poor Design: The framework being developed may not always follow correct 
  design patterns and that may lead to various problems like: StackOverflow, 
  Memory Leaks because of various factors.
- Multiple Threads: JBehave and Selenium tend to run across multiple threads 
  and managing multiple threads could be an overhead for the testers.
- Configuration Overhead: Documentation for JBehave is not up to the mark 
  and creating a framework which can leverage JBehave and Selenium would be 
  fairly difficult.
  
This tool aims to tackle all these problems. Provide a nearly no 
configuration framework to work upon. The examples provide a standard for 
managing the tests. The code aims to consider the generic requirements of 
all the organisations and is flexible enough to be used for specific use 
cases as well, making sure the performance does not suffer.

## Advantages

---
- Nearly no configuration is required. For all configurations related to 
  JBehave and Selenium are already baked in. Clients need to provide minimal 
  information about the tests they wish to run along with other information.
- Enables Data Driven Testing. Same tests are required to be performed on a 
  varied number of test data. Creating repeated tests leads to redundancy, 
  rather, users may provide a CSV (comma separated values) in order to run 
  same scenario again and again.
- There is no need to have deep knowledge of JBehave in order to use the 
  framework. Just a limited knowledge shall be required.
  
## Basic Set-up

----
- Make sure you have at least JDK-8 and Maven installed on the systems.
  - [Install JDK](https://www.oracle.com/in/java/technologies/javase-downloads.html)
  - [Install Maven](http://maven.apache.org/install.html)
- Identify the version of your browser and download relevant drivers.
  - [Chrome Driver](https://chromedriver.chromium.org/)
  - [Gecko Driver](https://github.com/mozilla/geckodriver/releases) for Firefox.
  - [Microsoft Edge](https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/)
  - [Safari](https://developer.apple.com/documentation/webkit/testing_with_webdriver_in_safari)
  - [Opera](https://github.com/operasoftware/operachromiumdriver/releases)
  - [Internet Explorer](https://www.microsoft.com/en-us/download/details.aspx?id=44069)
- Create a folder to save the driver and reports like `/home/kaustav/OUR`.
- Run `mvn install`.
- Add dependency to your project created in the local maven repo. For maven 
  refer [this](https://github.com/kaustavsarkar/OpenUIRunner/blob/main/google-test/pom.xml).
- Create a main method to run your tests. [Reference](https://github.com/kaustavsarkar/OpenUIRunner/blob/main/google-test/src/main/java/org/our/example/OURTestRunner.java) .
### *In the pipeline*:
- [ ] Unit tests.
- [ ] Test with other drivers as well.
- [ ]Add more use case and tests to cover multiple scenarios and cover edge 
  cases.
- [ ] Ask users to provide timeouts.
- [ ] Add defaults to OurConfiguration.
- [ ] Dockerise the framework for it to be used across Operating Systems.
- [ ] Deployable to Cloud.