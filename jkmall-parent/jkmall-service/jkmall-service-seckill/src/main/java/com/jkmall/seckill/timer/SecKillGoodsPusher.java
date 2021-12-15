package com.jkmall.seckill.timer;

import com.jchen.entity.DateUtil;
import com.jkmall.seckill.dao.SeckillGoodsMapper;
import com.jkmall.seckill.pojo.SeckillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class SecKillGoodsPusher {

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Scheduled(cron = "0/30 * * * * ?")
    public void loadGoodsPushRedis(){

        List<Date> dateMenus = DateUtil.getDateMenus();
        for(Date date: dateMenus){
            String timeSpace = DateUtil.date2Str(date);

            Example example = new Example(SeckillGoods.class);
            Example.Criteria criteria = example.createCriteria();

            criteria.andEqualTo("status", "1");
            criteria.andGreaterThan("stockCount", 0);
            criteria.andGreaterThanOrEqualTo("startTime", date);
            criteria.andLessThan("endTime", DateUtil.addDateHour(date, 2));
            Set ids = redisTemplate.boundHashOps(timeSpace).keys();
            criteria.andNotIn("id", ids);
            List<SeckillGoods> seckillGoods = seckillGoodsMapper.selectByExample(example);
            for (SeckillGoods seckillGood: seckillGoods){

                redisTemplate.boundHashOps(timeSpace).put(seckillGood.getId(), seckillGood);
            }
        }
    }
}
