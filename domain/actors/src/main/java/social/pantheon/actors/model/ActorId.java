package social.pantheon.actors.model;

import lombok.Value;

import java.io.Serializable;

@Value
public class ActorId implements Serializable {
    String username;
    String domain;

    public String toString(){
        return username + "@" + domain;
    }
}
