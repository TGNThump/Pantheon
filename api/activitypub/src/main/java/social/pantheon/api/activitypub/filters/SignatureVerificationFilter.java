package social.pantheon.api.activitypub.filters;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import social.pantheon.model.queries.VerifySignatureForInput;
import social.pantheon.services.QueryService;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SignatureVerificationFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    private static final Pattern pattern = Pattern.compile("([a-z]+)=\"([^\"]+)\"", Pattern.CASE_INSENSITIVE);
    private static final MessageDigest digester = getDigester();
    private static final String REQUEST_TARGET = "(request-target)";

    private static MessageDigest getDigester(){
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private final QueryService queryService;

    @Override
    public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
        try {
            HttpHeaders headers = request.headers().asHttpHeaders();

            if (!headers.containsKey("signature")) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request not signed.");
            if (!headers.containsKey("date")) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Signed request date not present.");
            if (!checkTimeWindow(headers)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Signed request date outside acceptable time window.");

            String rawSignature = headers.getFirst("signature");
            Map<String, String> params = new HashMap<>();

            for (String part : rawSignature.split(",")){
                Matcher matcher = pattern.matcher(part);
                if (!matcher.matches()) continue;
                params.put(matcher.group(1), matcher.group(2));
            }

            if (!params.containsKey("keyId") || !params.containsKey("signature"))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Signature header does not contain required properties keyId and signature.");

            String keyId = params.get("keyId");
            String signature = params.get("signature");
            String signedHeaders = params.containsKey("headers") ? params.get("headers") : "date";

            String message =  Arrays.asList(signedHeaders.toLowerCase().split(" ")).stream().map(signedHeader -> {
                if (signedHeader.equals(REQUEST_TARGET)){
                    return REQUEST_TARGET + ": " + request.methodName().toLowerCase() + " " + request.path();
                } else if (signedHeader.equals("digest")){
                    digester.update(request.bodyToMono(ByteBuffer.class).block());
                    return "digest: " + Base64.getEncoder().encodeToString(digester.digest());
                } else {
                    return signedHeader + ": " + String.join(",", headers.get(signedHeader));
                }
            }).collect(Collectors.joining("\n"));

            Mono<Boolean> verify = queryService.mono(new VerifySignatureForInput(keyId, message, signature), Boolean.class);

            return verify.flatMap(verified -> {
                if (verified) return next.handle(request);
                else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request signature.");
            });
        } catch (Throwable e){
            return Mono.error(e);
        }
    }

    private boolean checkTimeWindow(HttpHeaders headers) {
        try{
            Instant timeSent = Instant.parse(headers.getFirst("date"));
            return timeSent.isAfter(Instant.now().minusSeconds(30)) && timeSent.isBefore(Instant.now().plusSeconds(30));
        } catch (DateTimeParseException | NullPointerException ex){ return false; }
    }
}
