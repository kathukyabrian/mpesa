package io.github.kathukyabrian.core.factory;


import io.github.kathukyabrian.config.ApplicationProperties;
import io.github.kathukyabrian.core.ServiceRepository;

/**
 * singleton factory for service repository
 */
public class ServiceRepositoryFactory {
    private static ServiceRepository serviceRepository;

    public static ServiceRepository getServiceRepository() {
        if (serviceRepository == null) {
            serviceRepository = new ServiceRepository();
            try {
                serviceRepository.init();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return serviceRepository;
    }

    public static ApplicationProperties getApplicationProperties() {
        ServiceRepository serviceRepository = getServiceRepository();
        return serviceRepository.getApplicationProperties();
    }
}
