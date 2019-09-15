package social.pantheon.api.webfinger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebFingerServer {

    public static void main(String[] args){
        SpringApplication.run(WebFingerServer.class, args);
    }
}
