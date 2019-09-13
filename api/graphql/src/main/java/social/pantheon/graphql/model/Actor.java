package social.pantheon.graphql.model;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import social.pantheon.aggregates.actors.dto.ActorDTO;
import social.pantheon.aggregates.actors.dto.RelationshipDTO;
import social.pantheon.aggregates.actors.queries.GetFollowersForActor;
import social.pantheon.aggregates.actors.queries.GetFollowingForActor;
import social.pantheon.graphql.services.QueryService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class Actor{
    @Delegate
    private final ActorDTO actorDTO;

    private @Autowired QueryService queryService;
    private @Autowired ObjectProvider<Relationship> relationshipProvider;

    public CompletableFuture<List<Relationship>> getFollowers(){
        return queryService.queryList(new GetFollowersForActor(getId()), RelationshipDTO.class, relationshipProvider);
    }

    public CompletableFuture<List<Relationship>> getFollowing(){
        return queryService.queryList(new GetFollowingForActor(getId()), RelationshipDTO.class, relationshipProvider);
    }
}
