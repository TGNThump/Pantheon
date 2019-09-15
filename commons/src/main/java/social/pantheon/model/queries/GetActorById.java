package social.pantheon.model.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import social.pantheon.model.value.ActorId;

import java.util.function.Function;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetActorById {
    ActorId id;
}
