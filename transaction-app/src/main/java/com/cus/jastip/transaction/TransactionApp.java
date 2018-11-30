package com.cus.jastip.transaction;

import com.cus.jastip.transaction.client.OAuth2InterceptedFeignConfiguration;
import com.cus.jastip.transaction.config.ApplicationProperties;
import com.cus.jastip.transaction.config.DefaultProfileUtil;
import com.cus.jastip.transaction.service.OfferingThreadService;
import com.cus.jastip.transaction.service.PostingThreadService;
import com.cus.jastip.transaction.service.TripThreadService;

import io.github.jhipster.config.JHipsterConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ComponentScan(
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OAuth2InterceptedFeignConfiguration.class)
)
@EnableAutoConfiguration(exclude = {MetricFilterAutoConfiguration.class, MetricRepositoryAutoConfiguration.class})
@EnableConfigurationProperties({LiquibaseProperties.class, ApplicationProperties.class})
@EnableDiscoveryClient
public class TransactionApp {

	@Autowired
	private PostingThreadService postingThreadService;

	@Autowired
	private TripThreadService tripThreadService;
	
	@Autowired
	private OfferingThreadService offeringThreadService;
	
    private static final Logger log = LoggerFactory.getLogger(TransactionApp.class);

    private final Environment env;

    public TransactionApp(Environment env) {
        this.env = env;
    }

    /**
     * Initializes TransactionApp.
     * <p>
     * Spring profiles can be configured with a program arguments --spring.profiles.active=your-active-profile
     * <p>
     * You can find more information on how profiles work with JHipster on <a href="http://www.jhipster.tech/profiles/">http://www.jhipster.tech/profiles/</a>.
     */
    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)) {
            log.error("You have misconfigured your application! It should not run " +
                "with both the 'dev' and 'prod' profiles at the same time.");
        }
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_CLOUD)) {
            log.error("You have misconfigured your application! It should not " +
                "run with both the 'dev' and 'cloud' profiles at the same time.");
        }
        
        ScheduledExecutorService execPost = Executors.newSingleThreadScheduledExecutor();
		execPost.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				System.out.println("=============================");
				System.out.println("thread Posting start");
				System.out.println("Number of active threads from the given thread Start : " + Thread.activeCount());
				postingThreadService.postingExpired();
				System.out.println("Number of active threads from the given thread End : " + Thread.activeCount());
				System.out.println("thread Posting end");
				System.out.println("=============================");
			}
		}, 20, 20, TimeUnit.SECONDS);

		ScheduledExecutorService execTrip = Executors.newSingleThreadScheduledExecutor();
		execTrip.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				System.out.println("=============================");
				System.out.println("thread Trip start");
				System.out.println("Number of active threads from the given thread Start : " + Thread.activeCount());
				tripThreadService.tripExpired();
				System.out.println("Number of active threads from the given thread End : " + Thread.activeCount());
				System.out.println("thread Trip end");
				System.out.println("=============================");
			}
		}, 20, 20, TimeUnit.SECONDS);
		
		ScheduledExecutorService execOffering = Executors.newSingleThreadScheduledExecutor();
		execOffering.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				System.out.println("=============================");
				System.out.println("thread Trip start");
				System.out.println("Number of active threads from the given thread Start : " + Thread.activeCount());
				offeringThreadService.offeringExpired();
				System.out.println("Number of active threads from the given thread End : " + Thread.activeCount());
				System.out.println("thread Trip end");
				System.out.println("=============================");
			}
		}, 20, 20, TimeUnit.SECONDS);
    }

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments
     * @throws UnknownHostException if the local host name could not be resolved into an address
     */
    public static void main(String[] args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(TransactionApp.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        log.info("\n----------------------------------------------------------\n\t" +
                "Application '{}' is running! Access URLs:\n\t" +
                "Local: \t\t{}://localhost:{}\n\t" +
                "External: \t{}://{}:{}\n\t" +
                "Profile(s): \t{}\n----------------------------------------------------------",
            env.getProperty("spring.application.name"),
            protocol,
            env.getProperty("server.port"),
            protocol,
            InetAddress.getLocalHost().getHostAddress(),
            env.getProperty("server.port"),
            env.getActiveProfiles());

        String configServerStatus = env.getProperty("configserver.status");
        log.info("\n----------------------------------------------------------\n\t" +
                "Config Server: \t{}\n----------------------------------------------------------",
            configServerStatus == null ? "Not found or not setup for this application" : configServerStatus);
    }
}
