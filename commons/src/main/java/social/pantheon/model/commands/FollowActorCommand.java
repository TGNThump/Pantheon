package social.pantheon.model.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import social.pantheon.model.value.ActorId;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowActorCommand {
    @TargetAggregateIdentifier
    @NotNull ActorId source;
    @NotNull ActorId target;
}
