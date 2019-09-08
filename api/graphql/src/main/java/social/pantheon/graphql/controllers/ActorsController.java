package social.pantheon.graphql.controllers;

import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Controller;
import social.pantheon.actors.commands.CreateActorCommand;
import social.pantheon.actors.commands.FollowActorCommand;
import social.pantheon.actors.commands.UnfollowActorCommand;
import social.pantheon.actors.model.ActorId;
import social.pantheon.views.actorgraph.dto.Actor;
import social.pantheon.views.actorgraph.queries.GetActorByIdQuery;

import java.util.concurrent.CompletableFuture;

@GraphQLApi
@Controller
@RequiredArgsConstructor
public class ActorsController {

    private final QueryGateway queryGateway;
    private final CommandGateway commandGateway;

    @GraphQLQuery
    public CompletableFuture<Actor> getActor(ActorId id){
        return queryGateway.query(new GetActorByIdQuery(id), Actor.class);
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
