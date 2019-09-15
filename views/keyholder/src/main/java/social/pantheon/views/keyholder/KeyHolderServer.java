package social.pantheon.views.keyholder;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j2
@SpringBootApplication
public class KeyHolderServer {

    public static void main(String[] args){
        SpringApplication.run(KeyHolderServer.class, args);
    }
}
