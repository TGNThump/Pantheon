package social.pantheon.model.value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.codec.binary.Base64;
import social.pantheon.jsonld.Context;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

@Data
@ToString
@NoArgsConstructor
@Context("https://w3id.org/security/v1")
public class PublicKey implements RSAPublicKey {

    @JsonIgnore
    @ToString.Exclude
    RSAPublicKey delegate;

    String id;
    String owner;
    String publicKeyPem;

    public PublicKey(String id, String owner, String publicKeyPem) {
        this.id = id;
        this.owner = owner;
        setPublicKeyPem(publicKeyPem);
    }

    public void setPublicKeyPem(String publicKeyPem){
        this.publicKeyPem = publicKeyPem.replaceAll("\\n", "");

        try {
            publicKeyPem = publicKeyPem.replaceAll("\\n|-{5}[A-Z ]*-{5}", "");
            byte[] encoded = Base64.decodeBase64(publicKeyPem);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            delegate = (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(encoded));
        } catch (InvalidKeySpecException ex){
            throw new IllegalArgumentException(ex);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    @JsonIgnore
    public BigInteger getPublicExponent() {
        return delegate.getPublicExponent();
    }

    @Override
    @JsonIgnore
    public String getAlgorithm() {
        return delegate.getAlgorithm();
    }

    @Override
    @JsonIgnore
    public String getFormat() {
        return delegate.getFormat();
    }

    @Override
    @JsonIgnore
    public byte[] getEncoded() {
        return delegate.getEncoded();
    }

    @Override
    @JsonIgnore
    public BigInteger getModulus() {
        return delegate.getModulus();
    }

    @Override
    @JsonIgnore
    public AlgorithmParameterSpec getParams(){
        return delegate.getParams();
    }
}