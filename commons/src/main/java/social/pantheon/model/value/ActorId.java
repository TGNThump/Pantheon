package social.pantheon.model.value;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import social.pantheon.model.queries.GetActorById;

import java.io.Serializable;
import java.util.function.Function;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActorId implements Serializable {
    String domain;
    String username;

    public static ActorId of(String id){
        String[] parts = id.split("@");
        return new ActorId(parts[1], parts[0]);
    }

    public String toString(){
        return username + "@" + domain;
    }
    public String getLocalUrl(){ return domain + "/@" + username; }

    public static class Provider {
        @Value("${pantheon.domain}") String domain;

        public ActorId get(String username){
            return new ActorId(domain, username);
        }
    }
}
