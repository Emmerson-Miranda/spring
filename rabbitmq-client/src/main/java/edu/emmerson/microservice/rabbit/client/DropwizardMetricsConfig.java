package edu.emmerson.microservice.rabbit.client;

import org.springframework.context.annotation.Configuration;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;

@Configuration
@EnableMetrics(proxyTargetClass = true)
public class DropwizardMetricsConfig extends MetricsConfigurerAdapter {

}
