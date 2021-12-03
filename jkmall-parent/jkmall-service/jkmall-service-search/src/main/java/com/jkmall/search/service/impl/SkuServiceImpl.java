package com.jkmall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.jchen.entity.Result;
import com.jkmall.goods.feign.SkuFeign;
import com.jkmall.goods.pojo.Sku;
import com.jkmall.search.dao.SkuMapper;
import com.jkmall.search.pojo.SkuInfo;
import com.jkmall.search.service.SkuService;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public void importSku() {
        //调用changgou-service-goods微服务
        Result<List<Sku>> skuListResult = skuFeign.findAll();
        //将数据转成search.Sku
        List<SkuInfo> skuInfos=  JSON.parseArray(JSON.toJSONString(skuListResult.getData()),SkuInfo.class);
        for(SkuInfo skuInfo:skuInfos){
            Map<String, Object> specMap= JSON.parseObject(skuInfo.getSpec()) ;
            skuInfo.setSpecMap(specMap);
        }
        skuMapper.saveAll(skuInfos);
    }

    public Map<String, Object> search(Map<String, String> searchMap){
        String keywords = searchMap.get("keywords");

        //创建查询对象
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //设置查询条件
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuCategoryGroup").field("categoryName"));
        nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("name", keywords));
        //构建查询语句
        NativeSearchQuery searchQuery = nativeSearchQueryBuilder.build();
        AggregatedPage<SkuInfo> skuPage = elasticsearchTemplate.queryForPage(searchQuery, SkuInfo.class);

        //获取分组结果
        StringTerms terms = (StringTerms) skuPage.getAggregation("skuCategoryGroup");

        List<String> categories = terms.getBuckets().stream().map(b -> b.getKeyAsString()).collect(Collectors.toList());

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("categories", categories);
        resultMap.put("rows", skuPage.getContent());
        resultMap.put("total", skuPage.getTotalElements());
        return resultMap;
    }
}
