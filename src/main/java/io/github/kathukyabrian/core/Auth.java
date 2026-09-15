package io.github.kathukyabrian.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kathukyabrian.config.ApplicationProperties;
import io.github.kathukyabrian.core.factory.ServiceRepositoryFactory;
import io.github.kathukyabrian.dto.AuthToken;
import io.github.kathukyabrian.dto.DarajaAuthResponse;
import io.github.kathukyabrian.util.DarajaUtil;
import io.github.kathukyabrian.util.HttpUtil;
import okhttp3.MediaType;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Auth {
    private static String defaultConsumerKey;

    private static final Map<String, AuthToken> authTokenMap = new ConcurrentHashMap<>();


    public static String getAccessToken(Logger logger) {
        // get default authtoken
        if (defaultConsumerKey == null) {
            return getAuth(logger);
        }

        AuthToken defaultAuthToken = authTokenMap.get(defaultConsumerKey);

        if (LocalDateTime.now().isBefore(defaultAuthToken.getNextRefreshTime())) {
            return defaultAuthToken.getAccessToken();
        } else {
            return getAuth(logger);
        }
    }

    public static String getAccessToken(String consumerKey, String consumerSecret, Logger logger) {
        AuthToken authToken = authTokenMap.get(consumerKey);

        if (authToken == null) {
            return getAuth(consumerKey, consumerSecret, logger);
        }

        if (LocalDateTime.now().isBefore(authToken.getNextRefreshTime())) {
            return authToken.getAccessToken();
        } else {
            return getAuth(consumerKey, consumerSecret, logger);
        }
    }

    private static String getAuth(Logger logger) {
        ApplicationProperties applicationProperties = ServiceRepositoryFactory.getApplicationProperties();
        String url = applicationProperties.getAuthUrl();
        String password = DarajaUtil.generateAccessToken(applicationProperties.getConsumerKey(), applicationProperties.getConsumerSecret());

        DarajaAuthResponse darajaAuthResponse = makeAuthRequest(url, password, logger);
        if (darajaAuthResponse != null) {
            AuthToken authToken = new AuthToken(LocalDateTime.now().plusMinutes(59), darajaAuthResponse.getAccessToken());
            defaultConsumerKey = applicationProperties.getConsumerKey();
            authTokenMap.put(defaultConsumerKey, authToken);
            return authToken.getAccessToken();
        }

        return null;
    }

    private static String getAuth(String consumerKey, String consumerSecret, Logger logger) {
        ApplicationProperties applicationProperties = ServiceRepositoryFactory.getApplicationProperties();
        String url = applicationProperties.getAuthUrl();

        String password = DarajaUtil.generateAccessToken(consumerKey, consumerSecret);

        DarajaAuthResponse darajaAuthResponse = makeAuthRequest(url, password, logger);

        if (darajaAuthResponse != null) {
            AuthToken authToken = new AuthToken(LocalDateTime.now().plusMinutes(59), darajaAuthResponse.getAccessToken());
            authTokenMap.put(consumerKey, authToken);
            return darajaAuthResponse.getAccessToken();
        }

        return null;
    }

    private static DarajaAuthResponse makeAuthRequest(String url, String password, Logger logger) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic " + password);

        try {
            String response = HttpUtil.get(url, headers, MediaType.get("application/json; charset=utf-8"));
            return new ObjectMapper().readValue(response, DarajaAuthResponse.class);
        } catch (Exception ex) {
            logger.error("system|encountered an error while getting auth", ex);
            return null;
        }
    }
}
