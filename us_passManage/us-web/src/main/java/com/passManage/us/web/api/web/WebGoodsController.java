package com.passManage.us.web.api.web;

import com.passManage.us.core.serialize.ResponseMsg;
import com.passManage.us.web.webservice.goods.service.WebGoodsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;

/**
 * author:www.magicalcoder.com
 * date:2018/8/23
 * function:
 */
@RestController
@RequestMapping("/web/goods/")
public class WebGoodsController {

    @Resource
    private WebGoodsService webGoodsService;


    @RequestMapping("info")
    public ResponseMsg info(){
        Jedis jedis;
        webGoodsService.getWebGoods(1L);
        return new ResponseMsg("ok");
    }

}
