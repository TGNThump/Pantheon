package social.pantheon.model.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import social.pantheon.model.value.ActorId;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetFollowersForActor {
    ActorId id;
}
