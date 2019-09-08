package social.pantheon.views.actorgraph;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class Neo4jConfiguration {

    @Bean
    GraphDatabaseFactory graphDatabaseFactory(){
        return new GraphDatabaseFactory();
    }

    @Bean
    GraphDatabaseService graphDatabaseService(GraphDatabaseFactory graphDatabaseFactory){
        return graphDatabaseFactory.newEmbeddedDatabase(new File("data"));
    }
}
