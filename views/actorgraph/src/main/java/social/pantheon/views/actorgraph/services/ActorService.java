package social.pantheon.views.actorgraph.services;

import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.Timestamp;
import org.axonframework.queryhandling.QueryHandler;
import org.neo4j.graphdb.*;
import org.springframework.stereotype.Service;
import social.pantheon.model.dto.ActorDTO;
import social.pantheon.model.dto.RelationshipDTO;
import social.pantheon.model.events.ActorCreatedEvent;
import social.pantheon.model.events.ActorFollowedEvent;
import social.pantheon.model.events.ActorUnfollowedEvent;
import social.pantheon.model.queries.GetActorById;
import social.pantheon.model.queries.GetFollowersForActor;
import social.pantheon.model.queries.GetFollowingForActor;
import social.pantheon.model.value.ActorId;
import social.pantheon.views.actorgraph.serializers.ActorSerializer;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.neo4j.graphdb.Direction.INCOMING;
import static org.neo4j.graphdb.Direction.OUTGOING;

@Log4j2
@Service
@RequiredArgsConstructor
public class ActorService {

    private static final Label ACTOR  = Label.label("ActorDTO");
    private static final String ID = "id";
    private static final String CREATED_AT = "createdAt";

    private static final RelationshipType FOLLOWS = RelationshipType.withName("Follows");
    private static final RelationshipType LIKES = RelationshipType.withName("Likes");

    private final GraphDatabaseService db;
    private final ActorSerializer actorSerializer;

    private Node getActor(ActorId id){
        return db.findNode(ACTOR, ID, id.toString());
    }

    @QueryHandler
    public ActorDTO on(GetActorById query){
        @Cleanup Transaction tx = db.beginTx();

        Node node = getActor(query.getId());

        tx.success();
        return actorSerializer.serialize(node);
    }

    @QueryHandler
    public List<RelationshipDTO> on(GetFollowersForActor query){
        List<RelationshipDTO> followers = new ArrayList<>();

        try (Transaction tx = db.beginTx()){
            Node node = getActor(query.getId());
            ActorDTO target = actorSerializer.serialize(node);
            if (node == null) return null;
            for (Relationship relationship : node.getRelationships(INCOMING, FOLLOWS)){
                Instant time = Instant.ofEpochMilli((Long) relationship.getProperty(CREATED_AT));
                ActorDTO source = actorSerializer.serialize(relationship.getStartNode());
                followers.add(new RelationshipDTO(source, target, time));
            }
            tx.success();
        }

        followers.sort(Comparator.comparing(r -> r.getCreatedAt()));

        return followers;
    }

    @QueryHandler
    public List<RelationshipDTO> on(GetFollowingForActor query){
        List<RelationshipDTO> following = new ArrayList<>();

        try (Transaction tx = db.beginTx()){
            Node node = getActor(query.getId());
            ActorDTO source = actorSerializer.serialize(node);
            if (node == null) return null;
            for (Relationship relationship : node.getRelationships(OUTGOING, FOLLOWS)){
                Instant time = Instant.ofEpochMilli((Long) relationship.getProperty(CREATED_AT));
                ActorDTO target = actorSerializer.serialize(relationship.getEndNode());
                following.add(new RelationshipDTO(source, target, time));
            }
            tx.success();
        }

        following.sort(Comparator.comparing(r -> ((RelationshipDTO) r).getCreatedAt()).reversed());
        return following;
    }

    @EventHandler
    public void on(ActorCreatedEvent event){
        log.info(event);
        try (Transaction tx = db.beginTx()) {
            Node actor = db.findNode(ACTOR, ID, event.getId().toString());
            if (actor == null) {
                actor = db.createNode(ACTOR);
                actor.setProperty(ID, event.getId().toString());
            } else throw new IllegalStateException();
            tx.success();
        }
    }

    @EventHandler
    public void on(ActorFollowedEvent event, @Timestamp Instant timestamp){
        log.info(event);
        try (Transaction tx = db.beginTx()) {
            Node source = db.findNode(ACTOR, ID, event.getSource().toString());
            Node target = db.findNode(ACTOR, ID, event.getTarget().toString());

            Relationship follows = source.createRelationshipTo(target, FOLLOWS);
            follows.setProperty(CREATED_AT, timestamp.toEpochMilli());
            tx.success();
        }
    }

    @EventHandler
    public void on(ActorUnfollowedEvent event){
        log.info(event);
        try (Transaction tx = db.beginTx()) {
            Node source = db.findNode(ACTOR, ID, event.getSource().toString());
            Node target = db.findNode(ACTOR, ID, event.getTarget().toString());

            if (source.getDegree(FOLLOWS, OUTGOING) < target.getDegree(FOLLOWS, INCOMING)){
                for (Relationship r : source.getRelationships(FOLLOWS, OUTGOING)){
                    if (r.getEndNode().equals(target)){
                        r.delete();
                        break;
                    }
                }
            } else {
                for (Relationship r : target.getRelationships(FOLLOWS, INCOMING)){
                    if (r.getStartNode().equals(source)){
                        r.delete();
                        break;
                    }
                }
            }
            tx.success();
        }
    }
}
