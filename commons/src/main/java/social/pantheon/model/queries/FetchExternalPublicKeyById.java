package social.pantheon.model.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FetchExternalPublicKeyById {
    String keyId;
}
