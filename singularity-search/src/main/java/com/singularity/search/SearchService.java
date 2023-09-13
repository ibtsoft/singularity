package com.singularity.search;

public class SearchService {

    private final SearchIndexRepository searchIndexRepository;

    public SearchService(final SearchIndexRepository searchIndexRepository) {
        this.searchIndexRepository = searchIndexRepository;
    }

    public SearchResult search(final String text) {
        return new SearchResult();
    }
}
