package social.pantheon.views.actorgraph.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.neo4j.graphdb.*;
import org.springframework.stereotype.Service;
import social.pantheon.actors.events.ActorCreatedEvent;
import social.pantheon.actors.events.ActorFollowedEvent;
import social.pantheon.actors.events.ActorUnfollowedEvent;
import social.pantheon.views.actorgraph.dto.Actor;
import social.pantheon.views.actorgraph.queries.GetActorByIdQuery;

import static org.neo4j.graphdb.Direction.INCOMING;
import static org.neo4j.graphdb.Direction.OUTGOING;

@Log4j2
@Service
@RequiredArgsConstructor
public class ActorService {

    private static final Label ACTOR  = Label.label("Actor");
    private static final String ID = "id";

    private static final RelationshipType FOLLOWS = RelationshipType.withName("Follows");
    private static final RelationshipType LIKES = RelationshipType.withName("Likes");

    private final GraphDatabaseService db;

    @QueryHandler
    public Actor on(GetActorByIdQuery query){
        log.info(query);
        try (Transaction tx = db.beginTx()) {
            Node node = db.findNode(ACTOR, ID, query.getId().toString());
            if (node == null) return null;
            Actor actor = new Actor();
            actor.setId(query.getId());
            actor.setFollowing(node.getDegree(FOLLOWS, OUTGOING));
            actor.setFollowers(node.getDegree(FOLLOWS, INCOMING));
            actor.setLikes(node.getDegree(LIKES, OUTGOING));
            actor.setPosts(node.getDegree(OUTGOING) - actor.getFollowing() - actor.getLikes());
            log.info(actor);
            tx.success();
            return actor;
        }
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
    public void on(ActorFollowedEvent event){
        log.info(event);
        try (Transaction tx = db.beginTx()) {
            Node source = db.findNode(ACTOR, ID, event.getSource().toString());
            Node target = db.findNode(ACTOR, ID, event.getTarget().toString());

            source.createRelationshipTo(target, FOLLOWS);
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
