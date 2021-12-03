package com.jkmall.search.dao;

import com.jkmall.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

public interface SkuMapper extends ElasticsearchRepository<SkuInfo,Long> {
}
