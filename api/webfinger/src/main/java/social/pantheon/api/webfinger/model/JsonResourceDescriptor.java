package social.pantheon.api.webfinger.model;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class JsonResourceDescriptor {

    private final String subject;
    private List<String> aliases = Lists.newArrayList();
    private Map<String, String> properties = new HashMap<>();
    private List<LinkRelation> links = Lists.newArrayList();

}
