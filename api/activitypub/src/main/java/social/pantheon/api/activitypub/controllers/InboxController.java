package social.pantheon.api.activitypub.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import social.pantheon.api.activitypub.filters.SignatureVerificationFilter;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Log4j2
@RestController
@RequiredArgsConstructor
public class InboxController {

    private final SignatureVerificationFilter signatureVerificationFilter;

    @Bean
    public RouterFunction<ServerResponse> route(SignatureVerificationFilter filter) {
        return RouterFunctions.route(POST("/@{username}/inbox").or(POST("/inbox")), this::processActivity).filter(signatureVerificationFilter);
    }

    public Mono<ServerResponse> processActivity(ServerRequest request){
        log.debug(request.bodyToMono(String.class));
        return ok().build();
    }
}
