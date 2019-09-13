package social.pantheon.graphql.model;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import social.pantheon.aggregates.actors.dto.RelationshipDTO;

import java.time.Instant;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class Relationship {

    private final RelationshipDTO relationship;

    private @Autowired ObjectProvider<Actor> actorProvider;

    public Actor getSource(){
        return actorProvider.getObject(relationship.getSource());
    }

    public Actor getTarget(){
        return actorProvider.getObject(relationship.getTarget());
    }

    public Instant getCreatedAt(){
        return relationship.getCreatedAt();
    }
}
