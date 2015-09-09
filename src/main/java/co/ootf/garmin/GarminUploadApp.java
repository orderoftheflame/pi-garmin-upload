package co.ootf.garmin;

import javastrava.api.v3.auth.AuthorisationService;
import javastrava.api.v3.auth.impl.retrofit.AuthorisationServiceImpl;
import javastrava.api.v3.auth.model.Token;
import javastrava.api.v3.model.StravaUploadResponse;
import javastrava.api.v3.service.Strava;
import javastrava.api.v3.service.exception.BadRequestException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

public class GarminUploadApp {

    public static final boolean PRIVATE_DEBUG = false;
    public static final String DATA_TYPE = "fit";
    private static final Log LOG = LogFactory.getLog(GarminUploadApp.class);
    private static PropertiesService propertiesService = new PropertiesService();

    public static void main(String[] args) {
        File activitiesFolder = null;
        String deviceName = null;
        Properties properties = null;

        if (args.length > 0) {
            String path = args[0];
            if (StringUtils.isEmpty(path)) {
                LOG.error("No path specified");
                System.exit(-1);
            }
            File devicePathFile = new File(path);
            deviceName = devicePathFile.getName();
            if (!path.endsWith("/")) {
                path = path + "/";
            }

            properties = getProperties(args);

            activitiesFolder = new File(path + properties.getProperty(PropertiesService.DEVICE_ACTIVITIES + deviceName));
        }

        int fileCount = 0;
        if (activitiesFolder != null && activitiesFolder.exists() && activitiesFolder.listFiles().length > 0) {
            Strava strava = getStravaInstance(properties);

            LOG.info("Device found: " + deviceName);
            for (File file : activitiesFolder.listFiles()) {

                LOG.info("Attempting to upload file #" + fileCount++ + " for device " + deviceName);

                if (file.getName().endsWith(".fit") || file.getName().endsWith(".gpx")) {
                    final StravaUploadResponse uploadResponse = strava.upload(null, null, null, PRIVATE_DEBUG, false, DATA_TYPE, null, file);

                    LOG.debug("*** UPLOAD RESPONSE: " + uploadResponse.getStatus());

                    if (uploadResponse.getError() == null) {
                        LOG.info("Upload appears to have been successful, deleting file.");
                        try {
                            Files.move(file.toPath(), file.toPath().resolveSibling(file.getName() + "_uploaded"));
                        } catch (IOException e) {
                            LOG.error("Failed to move", e);
                        }
                        file.renameTo(new File(file.getAbsolutePath() + "_uploaded"));
                    } else {
                        LOG.error("File failed to upload. Have not moved. Error: " + uploadResponse.getError() + " Status: " + uploadResponse.getStatus());
                    }
                }

            }

        } else {
            LOG.info("No files found on this device.");
        }
    }

    private static Properties getProperties(String[] args) {
        Properties properties;
        String propertiesLocation = null;
        if (args.length > 1) {
            propertiesLocation = args[1];
        }
        properties = propertiesService.getProperties(propertiesLocation);
        return properties;
    }

    private static Strava getStravaInstance(Properties properties) {
        Token token = null;

        int clientId = Integer.parseInt(properties.getProperty(PropertiesService.API_CLIENTID));
        String secret = properties.getProperty(PropertiesService.API_SECRET);
        String code = properties.getProperty(PropertiesService.API_CODE);

        try {

            AuthorisationService service = new AuthorisationServiceImpl();
            token = service.tokenExchange(clientId, secret, code);
        } catch (BadRequestException e) {
            LOG.error("Please register a Strava Application at \"https://www.strava.com/settings/api\"", e);
            System.exit(-1);
        }
        return new Strava(token);
    }

}
