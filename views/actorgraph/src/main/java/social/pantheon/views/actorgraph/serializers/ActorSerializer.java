package social.pantheon.views.actorgraph.serializers;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.springframework.stereotype.Component;
import social.pantheon.model.dto.ActorDTO;
import social.pantheon.model.value.ActorId;

import static org.neo4j.graphdb.Direction.INCOMING;
import static org.neo4j.graphdb.Direction.OUTGOING;

@Component
public class ActorSerializer implements NodeSerializer<ActorDTO> {

    private static final Label ACTOR  = Label.label("ActorDTO");
    private static final String ID = "id";

    private static final RelationshipType FOLLOWS = RelationshipType.withName("Follows");
    private static final RelationshipType LIKES = RelationshipType.withName("Likes");

    @Override
    public ActorDTO serialize(Node node) {
        if (node == null) return null;
        ActorDTO actor = new ActorDTO();

        actor.setId(ActorId.of((String) node.getProperty(ID)));
        actor.setFollowingCount(node.getDegree(FOLLOWS, OUTGOING));
        actor.setFollowersCount(node.getDegree(FOLLOWS, INCOMING));
        actor.setLikeCount(node.getDegree(LIKES, OUTGOING));
        actor.setPostCount(node.getDegree(OUTGOING) - actor.getFollowingCount() - actor.getLikeCount());

        return actor;
    }
}
