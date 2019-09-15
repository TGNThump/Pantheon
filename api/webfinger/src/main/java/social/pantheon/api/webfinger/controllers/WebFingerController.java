package social.pantheon.api.webfinger.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import social.pantheon.api.webfinger.converters.ActorToJRD;
import social.pantheon.api.webfinger.model.JsonResourceDescriptor;
import social.pantheon.model.dto.ActorDTO;
import social.pantheon.model.queries.GetActorById;
import social.pantheon.model.value.ActorId;
import social.pantheon.services.QueryService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
public class WebFingerController {

    @Value("${pantheon.domain}")
    String domain;

    private final QueryService queryService;
    private final ActorToJRD actorToJRD;

    @GetMapping(".well-known/webfinger")
    public Mono<JsonResourceDescriptor> getResource(@RequestParam String resource, @RequestParam(required = false) List<String> rel){
        if (resource.startsWith("acct:")) resource = resource.substring("acct:".length());
        ActorId id = ActorId.of(resource);

        if (!id.getDomain().equals(domain)) return Mono.empty();

        return queryService.mono(new GetActorById(id), ActorDTO.class).map(actorToJRD);
    }
}
