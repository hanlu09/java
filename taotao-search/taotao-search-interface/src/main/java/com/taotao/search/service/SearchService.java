package com.taotao.search.service;

import com.taotao.common.pojo.SearchResult;

public interface SearchService {

	SearchResult query(String queryString, Integer page, Integer rows) throws Exception;
}
