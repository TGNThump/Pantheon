package social.pantheon.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import social.pantheon.model.value.ActorId;

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
