package co.ootf.garmin;

import javastrava.api.v3.auth.AuthorisationService;
import javastrava.api.v3.auth.impl.retrofit.AuthorisationServiceImpl;
import javastrava.api.v3.auth.model.Token;
import javastrava.api.v3.model.StravaActivity;
import javastrava.api.v3.service.Strava;
import javastrava.api.v3.service.exception.BadRequestException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GarminUploadApp {

    private static final Log LOG = LogFactory.getLog(GarminUploadApp.class);

    private static PropertiesService propertiesService = new PropertiesService();

    public static void main(String[] args) {
        Token token = null;

        int clientId = Integer.parseInt(propertiesService.getProperties().getProperty("api.clientid"));
        String secret = propertiesService.getProperties().getProperty("api.secret");
        String code = propertiesService.getProperties().getProperty("api.code");

        try {

            AuthorisationService service = new AuthorisationServiceImpl();
            token = service.tokenExchange(clientId, secret, code);
        } catch (BadRequestException e) {
            LOG.error("Please register a Strava Application at \"https://www.strava.com/settings/api\"", e);
            System.exit(-1);
        }


        Strava strava = new Strava(token);

        StravaActivity activity = strava.getActivity(351013934);

        LOG.info(activity.getDistance());
        LOG.info(activity.getElapsedTime());
        LOG.info(activity.getName());
        LOG.info(strava.getAthlete(activity.getAthlete().getId()).getFirstname());
        LOG.info(activity.getGear().getName());


    }



}
