package io.github.kathukyabrian.core;

import io.github.kathukyabrian.config.ApplicationProperties;
import io.github.kathukyabrian.config.ConfigUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class ServiceRepository implements Runnable {

    private static final Logger log = LogManager.getLogger(ServiceRepository.class);
    private ApplicationProperties applicationProperties;
    private Thread thread;

    public void init(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        this.thread = new Thread(this, "mpesa");
    }

    public void init() {
        try {
            log.info("system|first time request|initializing");
            Properties properties = ConfigUtil.readConfig();
            ApplicationProperties applicationProperties = ConfigUtil.getProperties(properties);
            ConfigUtil.validateProperties(applicationProperties);
            this.applicationProperties = applicationProperties;
            this.thread = new Thread(this, "mpesa");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void start() {
        this.thread.start();
        log.info("system|started payment thread|ready for requests");
    }

    public void stop() {
        this.thread.interrupt();
    }

    public ApplicationProperties getApplicationProperties() {
        return applicationProperties;
    }


    @Override
    public void run() {
        log.info("started {} thread. waiting for requests...", Thread.currentThread().getName());
        while (true) {

        }
    }
}
