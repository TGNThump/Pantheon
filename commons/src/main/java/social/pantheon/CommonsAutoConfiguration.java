package social.pantheon;

import brave.sampler.Sampler;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import social.pantheon.model.value.ActorId;
import social.pantheon.services.QueryService;

@Configuration
public class CommonsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ActorId.Provider actorIdProvider(){
        return new ActorId.Provider();
    }

    @Bean
    @ConditionalOnMissingBean
    public QueryService queryService(QueryGateway queryGateway){
        return new QueryService(queryGateway);
    }

    @Bean
    @ConditionalOnMissingBean
    public Sampler getSampler(){
        return Sampler.ALWAYS_SAMPLE;
    }
}
