package org.mukulphougat.userindexerservice.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mukulphougat.userindexerservice.model.UserRegisteredEvent;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserIndexService {

    private final ElasticsearchClient esClient;
    private static final String INDEX = "users-index";

    // Index user document
    public void index(UserRegisteredEvent event) throws IOException {
        event.setTimestamp(System.currentTimeMillis()); // set current timestamp
        IndexRequest<UserRegisteredEvent> request = IndexRequest.of(i -> i
                .index(INDEX)
                .id(event.getId())
                .document(event)
        );
        esClient.index(request);
    }

    // Get user by ID
    @Cacheable(value = "userCache", key = "#id")
    public UserRegisteredEvent findById(String id) throws IOException {
        GetResponse<UserRegisteredEvent> response = esClient.get(g -> g
                .index(INDEX)
                .id(id), UserRegisteredEvent.class);

        if (response.found()) {
            return response.source();
        }
        return null;
    }

    // Search users by username (simple match query)
    @Cacheable(value = "userCache", key = "#username")
    public List<UserRegisteredEvent> searchByUsername(String username) throws IOException {
        Query query = Query.of(q -> q
                .match(MatchQuery.of(m -> m
                        .field("username")
                        .query(username)
                )));

        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index(INDEX)
                .query(query)
        );

        SearchResponse<UserRegisteredEvent> searchResponse = esClient.search(searchRequest, UserRegisteredEvent.class);

        return searchResponse.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }
    // Search users by email (simple match query)
    @Cacheable(value = "userCache", key = "#email")
    public List<UserRegisteredEvent> searchByEmail(String email) throws IOException {
        Query query = Query.of(q -> q
                .match(MatchQuery.of(m -> m
                        .field("email")
                        .query(email)
                )));

        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index(INDEX)
                .query(query)
        );

        SearchResponse<UserRegisteredEvent> searchResponse = esClient.search(searchRequest, UserRegisteredEvent.class);

        return searchResponse.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }
    @Cacheable(value = "userCache", key = "#limit + '-' + #offset")
    public List<UserRegisteredEvent> fetchUsers(int limit, int offset) throws IOException {
        log.info("Cache miss - fetching users from Elasticsearch (offset={}, limit={})", offset, limit);

        SearchResponse<UserRegisteredEvent> response = esClient.search(s -> s
                        .index(INDEX)
                        .from(offset)
                        .size(limit)
                        .sort(sort -> sort
                                .field(f -> f
                                        .field("timestamp")
                                        .order(SortOrder.Desc)
                                )
                        ),
                UserRegisteredEvent.class
        );

        return response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }

}
