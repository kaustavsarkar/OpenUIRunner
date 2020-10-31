package selenium.webdriver;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public abstract class DelegateWebDriverProvider implements WebDriverProvider {
    protected WebDriver driver;

    @Override
    public boolean saveScreenshotTo(String path) {
        if (driver instanceof TakesScreenshot) {
            File file = new File(path);
            byte[] bytes = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BYTES);
            file.getParentFile().mkdirs();
            try {
                if (file.exists()) {
                    if (path.contains("Copy")) {
                        int i = Integer.parseInt(path.split("Copy-")[0]);
                        String newPath = path.split("Copy-")[1];
                        file = new File(newPath + "-Copy-" + (++i) + ".png");
                    } else {
                        file = new File(path + "-Copy-1.png");
                    }


                }
                file.createNewFile();
                IOUtils.write(bytes, new FileOutputStream(file));
            } catch (IOException e) {
                throw new RuntimeException("Can't save file", e);
            }
            return true;
        }
        return false;
    }

    @Override
    public void saveSourceCode(String path) {
        File file = new File(path);
        String pageSource = driver.getPageSource();
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
            IOUtils.write(pageSource, new FileOutputStream(file),
                    StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Can't save source code file", e);
        }

    }
}
