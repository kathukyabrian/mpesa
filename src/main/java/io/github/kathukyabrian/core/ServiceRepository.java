package io.github.kathukyabrian.core;

import io.github.kathukyabrian.config.ApplicationProperties;
import io.github.kathukyabrian.config.ConfigUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class ServiceRepository {

    private static final Logger log = LogManager.getLogger(ServiceRepository.class);
    private ApplicationProperties applicationProperties;

    public void init(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public void init() {
        try {
            log.info("system|first time request|initializing");
            Properties properties = ConfigUtil.readConfig();
            ApplicationProperties applicationProperties = ConfigUtil.getProperties(properties);
            ConfigUtil.validateProperties(applicationProperties);
            this.applicationProperties = applicationProperties;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public ApplicationProperties getApplicationProperties() {
        return applicationProperties;
    }
}
