package configuration;

/**
 * @author: Kaustav Sarkar
 * @created: 5/20/2019
 */
public class ConfigException extends RuntimeException {

    public ConfigException() {
        super();
    }

    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(String message, Throwable ex) {
        super(message, ex);
    }
}
