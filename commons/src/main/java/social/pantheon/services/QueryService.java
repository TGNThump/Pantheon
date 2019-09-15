package social.pantheon.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.axonframework.messaging.responsetypes.MultipleInstancesResponseType;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

import static reactor.core.publisher.Mono.fromFuture;

@Log4j2
@RequiredArgsConstructor
public class QueryService {
    private final QueryGateway queryGateway;

    public <T> Mono<T> mono(Object query, Class<T> type){
        return fromFuture(queryGateway.query(query, type));
    }

    public <T> Flux<T> flux(Object query, Class<T> type){
        Mono<List<T>> mono = fromFuture(queryGateway.query(query, new MultipleInstancesResponseType<>(type)));
        return mono.flatMapIterable(Function.identity());
    }

    public <I,O> Mono<O> mono(Object query, Class<I> type, ObjectProvider<O> objectProvider){
        return mono(query, type).map(ifNotNull(objectProvider::getObject));
    }

    public <I,O> Flux<O> flux(Object query, Class<I> type, ObjectProvider<O> objectProvider){
        return flux(query, type).map(ifNotNull(objectProvider::getObject));
    }

    private <I,O> Function<I,O> ifNotNull(Function<I,O> func){
        return dto -> dto == null ? null : func.apply(dto);
    }
}
