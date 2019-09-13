package social.pantheon.aggregates.actors.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import social.pantheon.aggregates.actors.value.ActorId;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetFollowingForActor {
    ActorId id;
}
