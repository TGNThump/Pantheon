package social.pantheon.views.actorgraph.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import social.pantheon.actors.model.ActorId;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetActorByIdQuery {
    ActorId id;
}
