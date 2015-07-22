package co.ootf.garmin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesService {

    private static final Log LOG = LogFactory.getLog(PropertiesService.class);
    private Properties properties;

    public Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            String propFileName = "api.properties";
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);) {


                if (inputStream != null) {
                    properties.load(inputStream);
                } else {
                    throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
                }
            } catch (IOException e) {
                LOG.error("Exception: " + e);
            }
        }
        return properties;
    }
}
