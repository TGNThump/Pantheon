package social.pantheon.cloud.gateway;

import brave.sampler.Sampler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

import static java.util.regex.Pattern.quote;

@SpringBootApplication
public class GatewayServer {

    private static final String AP_CONTENT_TYPE = "^.*(" + quote("application/activity+json") + "|" + quote("application/ld+json") + ").*$";

    public static void main(String[] args) {
        SpringApplication.run(GatewayServer.class, args);
    }

    @Bean
    public Sampler getSampler(){
        return Sampler.ALWAYS_SAMPLE;
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder){
        return builder.routes()
                .route(p -> p.path("/.well-known/webfinger").uri("lb://webfinger"))
                .route(p -> p.path("/.well-known/nodeinfo").uri("lb://nodeinfo"))
                .route(p -> p.path("/graphql").uri("lb://graphql"))
                .route(p -> p.header("content-type", AP_CONTENT_TYPE).or().header("accept", AP_CONTENT_TYPE).uri("lb://activitypub"))
                .build();
    }
}
