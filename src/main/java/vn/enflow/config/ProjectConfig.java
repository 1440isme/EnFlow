package vn.enflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Configuration
public class ProjectConfig {

    /**
     * Provide a system Clock bean to allow easier testing and consistent time references.
     */
    @Bean
    public Clock systemClock() {
        return Clock.systemDefaultZone();
    }

    /**
     * Provide a default DateTimeFormatter for the application (ISO_LOCAL_DATE_TIME).
     */
    @Bean
    public DateTimeFormatter dateTimeFormatter() {
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    }

    /**
     * Provide a default locale (Vietnamese) for any locale-sensitive beans.
     */
    @Bean
    public Locale defaultLocale() {
        return Locale.forLanguageTag("vi-VN");
    }
}
