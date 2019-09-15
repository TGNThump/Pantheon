package social.pantheon.api.webfinger.converters;

import org.springframework.stereotype.Component;
import social.pantheon.api.webfinger.model.JsonResourceDescriptor;
import social.pantheon.api.webfinger.model.LinkRelation;
import social.pantheon.model.dto.ActorDTO;

import java.util.List;
import java.util.function.Function;

@Component
public class ActorToJRD implements Function<ActorDTO, JsonResourceDescriptor> {

    @Override
    public JsonResourceDescriptor apply(ActorDTO actor) {
        JsonResourceDescriptor jrd = new JsonResourceDescriptor("acct:" + actor.getId());

        jrd.setAliases(List.of(
            "https://" + actor.getId().getLocalUrl()
        ));

        jrd.setLinks(List.of(
            new LinkRelation("http://webfinger.net/rel/profile-page", "text/html", "https://" + actor.getId().getLocalUrl()),
            new LinkRelation("self", "application/activity+json", "https://" + actor.getId().getLocalUrl())
        ));

        return jrd;
    }
}
