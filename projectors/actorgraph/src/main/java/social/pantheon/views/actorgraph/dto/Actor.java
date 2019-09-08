package social.pantheon.views.actorgraph.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import social.pantheon.actors.model.ActorId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Actor {
    ActorId id;
    Integer following;
    Integer followers;
    Integer likes;
    Integer posts;
}
