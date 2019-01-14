package com.neuedu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.ShippingMapper;
import com.neuedu.pojo.Shipping;
import com.neuedu.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AddressServiceImpl implements IAddressService {

    @Autowired
    ShippingMapper shippingMapper;

    /**
     * 添加收货地址
     */
    @Override
    public ServerResponse add(Integer userId, Shipping shipping) {
        //step1：参数非空校验
        if (shipping==null){
            return ServerResponse.createServerResponseByError("参数错误");
        }
        //step2：添加
        shipping.setUserId(userId);
        shippingMapper.insert(shipping);
        //step1：返回结果
        Map<String,Integer> map = Maps.newHashMap();
        map.put("shippingId",shipping.getId());
        return ServerResponse.createServerResponseBySuccess(null,map);
    }

    /**
     * 删除收货地址
     */
    @Override
    public ServerResponse del(Integer userId, Integer shippingId) {
        //step1：参数的非空校验
        if (shippingId==null){
            return ServerResponse.createServerResponseByError("参数错误");
        }
        //step2：删除
        int i = shippingMapper.deleteByUserIdAndShippingId(userId, shippingId);
        //step3：返回结果
        if (i>0){
            return ServerResponse.createServerResponseBySuccess("删除成功");
        }
        return ServerResponse.createServerResponseByError("删除失败");
    }

    /**
     * 登录状态下更新地址
     */
    @Override
    public ServerResponse update(Shipping shipping) {
        //step1：参数的非空校验
        if (shipping==null){
            return ServerResponse.createServerResponseByError("参数错误");
        }
       //step2:更新
        int update = shippingMapper.updateBySelectiveKey(shipping);
        if (update>0){
            return ServerResponse.createServerResponseBySuccess("更新成功");
        }
        //step3：返回结果
        return ServerResponse.createServerResponseByError("更新失败");

    }
    /**
     * 查看具体的地址
     */
    @Override
    public ServerResponse select(Integer userId, Integer shippingId) {
        //step1：参数的非空校验
        if (shippingId==null){
            return ServerResponse.createServerResponseByError("参数错误");
        }
        Shipping shipping = shippingMapper.selectByPrimaryKey(shippingId);
        return ServerResponse.createServerResponseBySuccess(null,shipping);
    }
    /**
     * 分页查询
     */
    @Override
    public ServerResponse list(Integer userId,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippings = shippingMapper.selectOneUserAddress(userId);
        PageInfo pageInfo = new PageInfo(shippings);
        return ServerResponse.createServerResponseBySuccess(null,pageInfo);
    }


}
