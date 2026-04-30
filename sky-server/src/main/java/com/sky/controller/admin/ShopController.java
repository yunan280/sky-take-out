package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {

    //提出常量
    public static final String KEY = "SHOP_STATUS";

    //注入RedisTemplate 对象用于操作Redis数据库；
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置店铺营业状态
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("设置营业状态")
    public Result setStatus(@PathVariable Integer status) {
        log.info("设置店铺营业状态：{}", status == 1 ? "营业中" : "打烊中");
        // 将状态保存到Redis中，键为"SHOP_STATUS"，值为status参数
        redisTemplate.opsForValue().set(KEY, status);


        return Result.success();
    }
    /**
     * 获取营业状态
     *
     * @return 营业状态
     */
    @GetMapping("/status")
    @ApiOperation("获取营业状态")
    public Result<Integer> getStatus() {
        log.info("获取营业状态");
        // 从Redis中获取营业状态，如果没有设置则默认为1（营业中）
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);//强转类型转换为Integer类型
        return Result.success( status);
    }
}
