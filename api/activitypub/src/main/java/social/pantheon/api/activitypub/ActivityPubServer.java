package social.pantheon.api.activitypub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ActivityPubServer {

    public static void main(String[] args){
        SpringApplication.run(ActivityPubServer.class, args);
    }
}
