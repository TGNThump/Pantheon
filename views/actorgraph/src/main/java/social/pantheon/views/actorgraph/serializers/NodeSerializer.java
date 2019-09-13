package social.pantheon.views.actorgraph.serializers;

import org.neo4j.graphdb.Node;

public interface NodeSerializer<T> {

    T serialize(Node node);
}
