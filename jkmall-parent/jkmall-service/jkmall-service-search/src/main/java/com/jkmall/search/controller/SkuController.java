package com.jkmall.search.controller;

import com.jchen.entity.Result;
import com.jchen.entity.StatusCode;
import com.jkmall.search.pojo.SkuInfo;
import com.jkmall.search.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/search")
@CrossOrigin
public class SkuController {

    @Autowired
    private SkuService skuService;

    /**
     * 导入数据
     * @return
     */
    @GetMapping("/import")
    public Result searchImport(){
        skuService.importSku();
        return new Result(true, StatusCode.OK,"导入数据到索引库中成功！");
    }

    @PostMapping
    public Result<Map<String, Object>> search(@RequestBody Map<String, String> queryMap){
        Map<String, Object> searchResult = skuService.search(queryMap);
        return new Result<Map<String, Object>>(true, StatusCode.OK, "Success", searchResult);
    }
}