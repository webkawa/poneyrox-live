package com.akasoft.poneyrox.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

/**
 *  Configuration du gestionnaire de taches.
 */
@Configuration
@EnableScheduling
public class TaskSchedulerConfiguration extends ThreadPoolTaskScheduler {
    /**
     *  Constructeur.
     */
    public TaskSchedulerConfiguration() {
        super();
        super.setPoolSize(8);
        super.setThreadNamePrefix("poneyrox");
    }
}
