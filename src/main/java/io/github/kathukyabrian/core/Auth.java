package io.github.kathukyabrian.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kathukyabrian.config.ApplicationProperties;
import io.github.kathukyabrian.core.factory.ServiceRepositoryFactory;
import io.github.kathukyabrian.dto.DarajaAuthResponse;
import io.github.kathukyabrian.util.DarajaUtil;
import io.github.kathukyabrian.util.HttpUtil;
import okhttp3.MediaType;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Auth {
    private static LocalDateTime nextRefreshTime;
    private static String accessToken;


    public static String getAccessToken(Logger logger) {
        if (nextRefreshTime == null) {
            return getAuth(logger);
        }

        if (LocalDateTime.now().isBefore(nextRefreshTime)) {
            return accessToken;
        } else {
            return getAuth(logger);
        }
    }

    private static String getAuth(Logger logger) {
        ApplicationProperties applicationProperties = ServiceRepositoryFactory.getApplicationProperties();
        String url = applicationProperties.getAuthUrl();
        String password = DarajaUtil.generateAccessToken(applicationProperties.getConsumerKey(), applicationProperties.getConsumerSecret());

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic " + password);

        String response;
        try {
            response = HttpUtil.get(url, headers, MediaType.get("application/json; charset=utf-8"));
            DarajaAuthResponse darajaAuthResponse = new ObjectMapper().readValue(response, DarajaAuthResponse.class);
            if (darajaAuthResponse != null) {
                accessToken = darajaAuthResponse.getAccessToken();
                nextRefreshTime = LocalDateTime.now().plusMinutes(59);
            }
            return accessToken;
        } catch (IOException e) {
            logger.error("system|encountered an error while getting auth", e);
            return null;
        }
    }
}
