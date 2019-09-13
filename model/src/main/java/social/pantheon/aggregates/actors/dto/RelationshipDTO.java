package social.pantheon.aggregates.actors.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationshipDTO {
    ActorDTO source;
    ActorDTO target;
    Instant createdAt;
}
