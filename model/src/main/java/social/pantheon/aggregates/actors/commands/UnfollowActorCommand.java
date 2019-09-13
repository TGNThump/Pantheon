package social.pantheon.aggregates.actors.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import social.pantheon.aggregates.actors.value.ActorId;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnfollowActorCommand {
    @TargetAggregateIdentifier
    @NotNull ActorId source;
    @NotNull ActorId target;
}
