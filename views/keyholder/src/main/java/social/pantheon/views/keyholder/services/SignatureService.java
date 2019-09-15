package social.pantheon.views.keyholder.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Base64;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.vault.VaultException;
import org.springframework.vault.core.VaultOperations;
import social.pantheon.model.queries.GetPublicKeyById;
import social.pantheon.model.queries.GetSignatureForInput;
import social.pantheon.model.queries.VerifySignatureForInput;
import social.pantheon.model.value.PublicKey;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.util.LinkedHashMap;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class SignatureService {

    private final VaultOperations vaultOperations;
    private final KeyService keyService;

    @QueryHandler
    public String handle(GetSignatureForInput query) throws VaultException {
        log.debug("Handling " + query);

        Map<String, Object> request = new LinkedHashMap<>();
        request.put("input", Base64Utils.encodeToString(query.getMessage().getBytes()));
        request.put("signature_algorithm", "pkcs1v15");

        return ((String) vaultOperations
                    .write(String.format("%s/sign/%s", "transit", keyService.getUsernameFromKeyId(query.getKeyId())), request)
                    .getRequiredData().get("signature")).substring("vault:v1:".length());
    }

    @QueryHandler
    public boolean handle(VerifySignatureForInput query) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        log.debug("Handling " + query);
        PublicKey publicKey = keyService.handle(new GetPublicKeyById(query.getKeyId()));

        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initVerify(publicKey);
        sign.update(query.getMessage().getBytes());
        return sign.verify(Base64.decodeBase64(query.getSignature().getBytes()));
    }
}
