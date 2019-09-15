package social.pantheon.views.keyholder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.core.VaultKeyValueOperations;
import org.springframework.vault.core.VaultKeyValueOperationsSupport;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.core.VaultTransitOperations;

@Configuration
public class VaultConfiguration {

    @Bean
    public VaultTransitOperations vaultTransitOperations(VaultTemplate vaultTemplate){
        return vaultTemplate.opsForTransit("transit");
    }

    @Bean
    public VaultKeyValueOperations vaultKeyValueOperations(VaultTemplate vaultTemplate){
        return vaultTemplate.opsForKeyValue("kv", VaultKeyValueOperationsSupport.KeyValueBackend.KV_2);
    }
}
