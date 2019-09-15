package social.pantheon.graphql.model;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import social.pantheon.model.dto.ActorDTO;
import social.pantheon.model.dto.RelationshipDTO;
import social.pantheon.model.queries.GetFollowersForActor;
import social.pantheon.model.queries.GetFollowingForActor;
import social.pantheon.services.QueryService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class Actor{
    @Delegate
    private final ActorDTO actorDTO;

    @Autowired
    private QueryService queryService;

    private @Autowired ObjectProvider<Relationship> relationshipProvider;

    public Flux<Relationship> getFollowers(){
        return queryService.flux(new GetFollowersForActor(getId()), RelationshipDTO.class, relationshipProvider);
    }

    public Flux<Relationship> getFollowing(){
        return queryService.flux(new GetFollowingForActor(getId()), RelationshipDTO.class, relationshipProvider);
    }
}
