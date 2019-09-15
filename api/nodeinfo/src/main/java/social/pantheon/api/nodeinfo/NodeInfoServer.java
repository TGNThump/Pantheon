package social.pantheon.api.nodeinfo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NodeInfoServer {

    public static void main(String[] args){
        SpringApplication.run(NodeInfoServer.class, args);
    }
}
