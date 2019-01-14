package com.neuedu.controller.backend;

import com.github.pagehelper.PageHelper;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 后台商品接口
 */
@RestController
@RequestMapping(value = "/manage/product")
public class ProductManageController {

    @Autowired
    IProductService productService;

    /**
     * 新增或更新产品
     */
    @RequestMapping(value = "/save.do")
    public ServerResponse saveOrUpdate(HttpSession session, Product product){
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENTUSER);
        //判断用户是否登录
        if (userInfo==null){
            return ServerResponse.createServerResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if (userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.createServerResponseByError(Const.ResponseCodeEnum.NO_PRIVILEGE.getCode(),Const.ResponseCodeEnum.NO_PRIVILEGE.getDesc());
        }
        return productService.saveOrUpdate(product);
    }
    /**
     * 产品上下架
     */
    @RequestMapping(value = "/set_sale_status.do")
    public ServerResponse set_sale_status(HttpSession session, Integer productId,Integer status){
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENTUSER);
        //判断用户是否登录
        if (userInfo==null){
            return ServerResponse.createServerResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if (userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.createServerResponseByError(Const.ResponseCodeEnum.NO_PRIVILEGE.getCode(),Const.ResponseCodeEnum.NO_PRIVILEGE.getDesc());
        }
        return productService.set_sale_status(productId,status);
    }

    /**
     * 查看商品的详情
     */
    @RequestMapping(value = "/detail.do")
    public ServerResponse detail(HttpSession session, Integer productId){
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENTUSER);
        //判断用户是否登录
        if (userInfo==null){
            return ServerResponse.createServerResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if (userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.createServerResponseByError(Const.ResponseCodeEnum.NO_PRIVILEGE.getCode(),Const.ResponseCodeEnum.NO_PRIVILEGE.getDesc());
        }
        return productService.detail(productId);
    }

    /**
     * 后台-分页查看商品列表
     */
    @RequestMapping(value = "/list.do")
    public ServerResponse<Product> findAll(HttpSession session,
                                           @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                                           @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENTUSER);
        //判断用户是否登录
        if (userInfo==null){
            return ServerResponse.createServerResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if (userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.createServerResponseByError(Const.ResponseCodeEnum.NO_PRIVILEGE.getCode(),Const.ResponseCodeEnum.NO_PRIVILEGE.getDesc());
        }
        return productService.findAll(pageNum,pageSize);
    }

    /**
     * 后台-商品搜索 分页
     */
    @RequestMapping(value = "/search.do")
    public ServerResponse<Product> search(HttpSession session,
                                           @RequestParam(required = false) Integer productId,
                                           @RequestParam(required = false) String productName,
                                           @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                                           @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENTUSER);
        //判断用户是否登录
        if (userInfo==null){
            return ServerResponse.createServerResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if (userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.createServerResponseByError(Const.ResponseCodeEnum.NO_PRIVILEGE.getCode(),Const.ResponseCodeEnum.NO_PRIVILEGE.getDesc());
        }
        return productService.search(productId,productName,pageNum,pageSize);
    }

}
