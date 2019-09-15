package social.pantheon.views.keyholder.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultKeyValueOperations;
import org.springframework.vault.core.VaultTransitOperations;
import org.springframework.vault.support.VaultResponseSupport;
import org.springframework.vault.support.VaultTransitKey;
import org.springframework.vault.support.VaultTransitKeyCreationRequest;
import social.pantheon.model.events.ActorCreatedEvent;
import social.pantheon.model.queries.FetchExternalPublicKeyById;
import social.pantheon.model.queries.GetPublicKeyById;
import social.pantheon.model.value.PublicKey;
import social.pantheon.services.QueryService;

import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;

@Log4j2
@Service
@RequiredArgsConstructor
public class KeyService {

    private final VaultTransitOperations transit;
    private final VaultKeyValueOperations kv;
    private final QueryService queryService;

    @Value("${pantheon.domain}")
    String localDomain;

    @EventHandler
    public void on(ActorCreatedEvent event){
        log.debug("Handling " + event);
        if (!event.getId().getDomain().equals(localDomain)) return;
        log.info("Creating keypair for " + event.getId());
        transit.createKey(event.getId().getUsername(), VaultTransitKeyCreationRequest.ofKeyType("rsa-2048"));
    }

    @QueryHandler
    public PublicKey handle(GetPublicKeyById query){
        log.debug("Handling " + query);
        String keyId = query.getKeyId().replaceAll("^https?:\\/\\/", "");

        if (keyId.startsWith(localDomain)) return getLocalKey(keyId);
        else return getExternalKey(keyId);
    }

    private PublicKey getLocalKey(String keyId){
        log.debug("Getting PublicKey From Vault Transit: " + keyId);
        String username = getUsernameFromKeyId(keyId);

        VaultTransitKey key = transit.getKey(username);
        String pem = (String) ((LinkedHashMap) key.getKeys().get(String.valueOf(key.getLatestVersion()))).get("public_key");

        return new PublicKey(keyId, localDomain + "/@" + username, pem);
    }

    private PublicKey getExternalKey(String keyId){
        VaultResponseSupport<PublicKey> result = kv.get(keyId, PublicKey.class);
        if (result != null){
            log.debug("Getting PublicKey From Vault KV: " + keyId);
            return result.getRequiredData();
        }

        log.debug("Getting PublicKey From ActivityPub: " + keyId);
        PublicKey pk = queryService.mono(new FetchExternalPublicKeyById(keyId), PublicKey.class).block();
        kv.put(pk.getId(), pk);
        return pk;
    }

    String getUsernameFromKeyId(String keyId) {
        return keyId.substring(localDomain.length() + 2, keyId.contains("#") ? keyId.indexOf("#") : keyId.length());
    }
}
