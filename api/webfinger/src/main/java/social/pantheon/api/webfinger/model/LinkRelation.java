package social.pantheon.api.webfinger.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class LinkRelation {

    private final String rel;
    private final String type;
    private final String href;
    private Map<String, String> titles = new HashMap<>();
    private Map<String, String> properties = new HashMap<>();

}
