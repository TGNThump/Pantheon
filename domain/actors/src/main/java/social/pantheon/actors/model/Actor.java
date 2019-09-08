package social.pantheon.actors.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.context.annotation.Profile;
import social.pantheon.actors.commands.CreateActorCommand;
import social.pantheon.actors.commands.FollowActorCommand;
import social.pantheon.actors.commands.UnfollowActorCommand;
import social.pantheon.actors.events.ActorCreatedEvent;
import social.pantheon.actors.events.ActorFollowedEvent;
import social.pantheon.actors.events.ActorUnfollowedEvent;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Log4j2
@Aggregate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Profile("command")
public class Actor {

    @AggregateIdentifier
    ActorId id;

    List<ActorId> blockedActors;
    List<ActorId> following;

    @CommandHandler
    public Actor(@Valid CreateActorCommand command) {
        apply(new ActorCreatedEvent(command.getId()));
    }

    @EventSourcingHandler
    public void on(ActorCreatedEvent event){
        id = event.getId();
        blockedActors = new ArrayList<>();
        following = new ArrayList<>();
    }

    @CommandHandler
    public void handle(@Valid FollowActorCommand command){
        if (following.contains(command.getTarget())) return;
        apply(new ActorFollowedEvent(command.getSource(), command.getTarget()));
    }

    @EventSourcingHandler
    public void on(ActorFollowedEvent event){
        following.add(event.getTarget());
    }

    @CommandHandler
    public void handle(@Valid UnfollowActorCommand command){
        if (!following.contains(command.getTarget())) return;
        apply(new ActorUnfollowedEvent(command.getSource(), command.getTarget()));
    }

    @EventSourcingHandler
    public void on(ActorUnfollowedEvent event){
        following.remove(event.getTarget());
    }
}
