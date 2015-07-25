package co.ootf.garmin;

import javastrava.api.v3.auth.AuthorisationService;
import javastrava.api.v3.auth.impl.retrofit.AuthorisationServiceImpl;
import javastrava.api.v3.auth.model.Token;
import javastrava.api.v3.model.StravaActivity;
import javastrava.api.v3.model.StravaUploadResponse;
import javastrava.api.v3.service.Strava;
import javastrava.api.v3.service.exception.BadRequestException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.Arrays;

public class GarminUploadApp {

    private static final Log LOG = LogFactory.getLog(GarminUploadApp.class);
    public static final boolean PRIVATE_DEBUG = true;
    public static final String DATA_TYPE = "fit";

    private static PropertiesService propertiesService = new PropertiesService();

    public static void main(String[] args) {
        File activitiesFolder = null;
        String deviceName = null;
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
            activitiesFolder = new File(path + propertiesService.getProperties().getProperty(PropertiesService.DEVICE_ACTIVITIES + deviceName));
        }


        LOG.info(deviceName);

        Token token = null;

        int clientId = Integer.parseInt(propertiesService.getProperties().getProperty(PropertiesService.API_CLIENTID));
        String secret = propertiesService.getProperties().getProperty(PropertiesService.API_SECRET);
        String code = propertiesService.getProperties().getProperty(PropertiesService.API_CODE);

        try {

            AuthorisationService service = new AuthorisationServiceImpl();
            token = service.tokenExchange(clientId, secret, code);
        } catch (BadRequestException e) {
            LOG.error("Please register a Strava Application at \"https://www.strava.com/settings/api\"", e);
            System.exit(-1);
        }

        Strava strava = new Strava(token);
        int fileCount = 0;
        for (File file : activitiesFolder.listFiles()) {

            LOG.info("Attempting to upload file #" + fileCount++ + " for device " + deviceName);

            final StravaUploadResponse uploadResponse = strava.upload(null, null, null, PRIVATE_DEBUG, false, DATA_TYPE, null, file);

            LOG.debug("*** UPLOAD RESPONSE: " + uploadResponse.getStatus());
            LOG.debug("*** UPLOAD ERROR: " + uploadResponse.getError());

            if (uploadResponse.getError() == null) {
                LOG.info("Upload appears to have been successful, deleting file.");
                file.delete();
            }

        }

    }

}
