package social.pantheon.actors.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import social.pantheon.actors.model.ActorId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActorFollowedEvent {
    ActorId source;
    ActorId target;
}
