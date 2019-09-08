package social.pantheon.actors;

import lombok.extern.log4j.Log4j2;
import org.axonframework.axonserver.connector.AxonServerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@Log4j2
@SpringBootApplication
@EnableDiscoveryClient
public class ActorsServer {

    public static void main(String[] args){
        SpringApplication.run(ActorsServer.class, args);
    }

}
