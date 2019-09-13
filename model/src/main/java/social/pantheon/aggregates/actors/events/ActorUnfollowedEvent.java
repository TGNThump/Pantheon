package social.pantheon.aggregates.actors.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import social.pantheon.aggregates.actors.value.ActorId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActorUnfollowedEvent {
    ActorId source;
    ActorId target;
}
