package social.pantheon.actors.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import social.pantheon.actors.model.ActorId;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateActorCommand {
    @TargetAggregateIdentifier
    @NotNull ActorId id;
}