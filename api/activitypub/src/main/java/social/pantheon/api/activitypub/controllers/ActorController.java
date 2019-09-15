package social.pantheon.api.activitypub.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import social.pantheon.api.activitypub.model.Actor;
import social.pantheon.model.dto.ActorDTO;
import social.pantheon.model.queries.GetActorById;
import social.pantheon.model.queries.GetPublicKeyById;
import social.pantheon.model.value.ActorId;
import social.pantheon.model.value.PublicKey;
import social.pantheon.services.QueryService;

@RestController
@RequiredArgsConstructor
public class ActorController {

    private final QueryService queryService;
    private final ActorId.Provider actorIdProvider;

    @GetMapping("/@{username}")
    public Mono<Actor> getActor(@PathVariable String username){
        ActorId actorId = actorIdProvider.get(username);

        Mono<ActorDTO> actorMono = queryService.mono(new GetActorById(actorId), ActorDTO.class);
        Mono<PublicKey> keyMono = queryService.mono(new GetPublicKeyById(actorId.getLocalUrl() + "#main-key"), PublicKey.class);

        return Mono.zip(actorMono, keyMono, (actorDTO, publicKey) -> {
            Actor actor = new Actor();
            actor.setId("https://" + actorDTO.getId().getLocalUrl());
            actor.setSharedInbox("https://" + actorDTO.getId().getDomain() + "/inbox");
            actor.setPreferredUsername(actorDTO.getId().getUsername());
            actor.setPublicKey(publicKey);
            return actor;
        });
    }
}
