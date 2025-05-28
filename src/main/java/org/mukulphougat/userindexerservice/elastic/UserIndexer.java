package org.mukulphougat.userindexerservice.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import lombok.RequiredArgsConstructor;
import org.mukulphougat.userindexerservice.model.UserRegisteredEvent;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserIndexer {

    private final ElasticsearchClient esClient;

    public void index(UserRegisteredEvent event) throws Exception {
        // Set current timestamp in milliseconds before indexing
        event.setTimestamp(System.currentTimeMillis());
        IndexRequest<UserRegisteredEvent> request = IndexRequest.of(i -> i
                .index("users-index")
                .id(event.getId())
                .document(event)
        );
        esClient.index(request);
    }
}