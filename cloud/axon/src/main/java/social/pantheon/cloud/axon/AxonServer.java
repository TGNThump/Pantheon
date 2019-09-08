package social.pantheon.cloud.axon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackageClasses = io.axoniq.axonserver.AxonServer.class)
@EnableAsync
@EnableScheduling
public class AxonServer extends io.axoniq.axonserver.AxonServer {

    public static void main(String[] args){
        SpringApplication.run(AxonServer.class, args);
    }
}
