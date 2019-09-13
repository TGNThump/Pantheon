package social.pantheon.graphql.services;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import lombok.extern.log4j.Log4j2;
import org.axonframework.messaging.responsetypes.MultipleInstancesResponseType;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Log4j2
@Component
@RequiredArgsConstructor
public class QueryService {
    @Delegate
    private final QueryGateway queryGateway;

    public <I,O> CompletableFuture<List<O>> queryList(Object query, Class<I> type, ObjectProvider<O> objectProvider){
        return queryGateway.query(query, new MultipleInstancesResponseType<>(type)).thenApply(list -> list.stream().map(objectProvider::getObject).collect(Collectors.toList()));
    }

    public <I,O> CompletableFuture<O> query(Object query, Class<I> type, ObjectProvider<O> objectProvider){
        return queryGateway.query(query, type).thenApply((dto) -> dto == null ? null : objectProvider.getObject(dto));
    }
}
