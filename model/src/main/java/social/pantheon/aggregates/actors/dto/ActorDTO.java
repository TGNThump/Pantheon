package social.pantheon.aggregates.actors.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import social.pantheon.aggregates.actors.value.ActorId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActorDTO {
    ActorId id;
    Integer followingCount;
    Integer followersCount;
    Integer likeCount;
    Integer postCount;
}
