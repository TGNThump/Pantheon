package social.pantheon.aggregates.actors.value;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActorId implements Serializable {
    String username;
    String domain;

    public static ActorId of(String id){
        String[] parts = id.split("@");
        return new ActorId(parts[0], parts[1]);
    }

    public String toString(){
        return username + "@" + domain;
    }
}
