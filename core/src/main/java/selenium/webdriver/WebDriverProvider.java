package selenium.webdriver;

import configuration.DriverName;
import org.openqa.selenium.WebDriver;

import java.util.Arrays;

public interface WebDriverProvider {

    WebDriver get();

    //void initialize(String browser);

    boolean saveScreenshotTo(String path);

    void saveSourceCode(String path);

    void end();

    @SuppressWarnings("serial")
	class WebDriverNotSupported extends RuntimeException {
        public WebDriverNotSupported(String browser) {
            super("The browser you provided does not exist or is yet not supported. \n"
                    +
                    "Please raise a request if you need this browser to be supported.\n"
                    + "Supported Browsers: "
                    + Arrays.toString(DriverName.values())
                    + "Browser value passed: " + browser);
        }
    }
}
