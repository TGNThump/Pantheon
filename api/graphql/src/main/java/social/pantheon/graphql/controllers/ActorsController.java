package social.pantheon.graphql.controllers;

import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;
import social.pantheon.model.commands.CreateActorCommand;
import social.pantheon.model.commands.FollowActorCommand;
import social.pantheon.model.commands.UnfollowActorCommand;
import social.pantheon.model.dto.ActorDTO;
import social.pantheon.model.queries.GetActorById;
import social.pantheon.model.value.ActorId;
import social.pantheon.graphql.model.Actor;
import social.pantheon.services.QueryService;

import java.util.concurrent.CompletableFuture;

@GraphQLApi
@Controller
@RequiredArgsConstructor
public class ActorsController {

    private final QueryService queryService;
    private final CommandGateway commandGateway;
    private final ObjectProvider<Actor> actorProvider;

    @GraphQLQuery
    public Mono<Actor> getActor(ActorId id){
        return queryService.mono(new GetActorById(id), ActorDTO.class, actorProvider);
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
