package social.pantheon.views.actorgraph;

import lombok.extern.log4j.Log4j2;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

@Log4j2
@Configuration
public class Neo4jConfiguration {

    @Value("${neo4j.clear_on_start:false}")
    boolean clearOnStart;

    @Bean
    GraphDatabaseService graphDatabaseService() throws IOException {
        if (clearOnStart){
            log.info("Clearing " + new File("data").getAbsolutePath());

            Files.walk(new File("data").toPath())
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }

        return new GraphDatabaseFactory().newEmbeddedDatabase(new File("data"));
    }
}