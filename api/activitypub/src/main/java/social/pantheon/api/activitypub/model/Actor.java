package social.pantheon.api.activitypub.model;

import lombok.Data;
import social.pantheon.jsonld.Context;
import social.pantheon.model.value.PublicKey;

@Data
@Context("https://www.w3.org/ns/activitystreams")
public class Actor {
    private String id;
    private String type = "Person";
    private String preferredUsername;
    private String summary;
    private String sharedInbox;

    private PublicKey publicKey;

    public String getInbox(){
        return getId() + "/inbox";
    }

    public String getOutbox(){
        return getId() + "/outbox";
    }

    public String getFollowing(){
        return getId() + "/following";
    }

    public String getFollowers(){
        return getId() + "/followers";
    }

    public String getLiked(){
        return getId() + "/liked";
    }
}
