package social.pantheon.model.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import social.pantheon.model.value.ActorId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActorFollowedEvent {
    ActorId source;
    ActorId target;
}
