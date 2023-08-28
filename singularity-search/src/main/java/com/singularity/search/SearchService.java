package com.singularity.search;

public class SearchService {

    private final SearchIndexRepository searchIndexRepository;

    public SearchService(SearchIndexRepository searchIndexRepository) {
        this.searchIndexRepository = searchIndexRepository;
    }

    public SearchResult search(String text) {
        return new SearchResult();
    }
}
