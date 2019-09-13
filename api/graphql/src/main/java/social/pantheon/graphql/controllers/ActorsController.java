package social.pantheon.graphql.controllers;

import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import social.pantheon.aggregates.actors.commands.CreateActorCommand;
import social.pantheon.aggregates.actors.commands.FollowActorCommand;
import social.pantheon.aggregates.actors.commands.UnfollowActorCommand;
import social.pantheon.aggregates.actors.dto.ActorDTO;
import social.pantheon.aggregates.actors.queries.GetActorById;
import social.pantheon.aggregates.actors.value.ActorId;
import social.pantheon.graphql.model.Actor;
import social.pantheon.graphql.services.QueryService;

import java.util.concurrent.CompletableFuture;

@GraphQLApi
@Controller
@RequiredArgsConstructor
public class ActorsController {

    private final QueryService queryService;
    private final CommandGateway commandGateway;
    private final ObjectProvider<Actor> actorProvider;

    @GraphQLQuery
    public CompletableFuture<Actor> getActor(ActorId id){
        return queryService.query(new GetActorById(id), ActorDTO.class, actorProvider);
    }

    @GraphQLMutation
    public boolean create(CreateActorCommand cmd){
       commandGateway.sendAndWait(cmd);
       return true;
    }

    @GraphQLMutation
    public boolean follow(FollowActorCommand cmd){
        commandGateway.sendAndWait(cmd);
        return true;
    }

    @GraphQLMutation
    public boolean unfollow(UnfollowActorCommand cmd){
        commandGateway.sendAndWait(cmd);
        return true;
    }
}
