package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

public interface IProductService {
    /**
     * 新增或更新产品
     */
    ServerResponse saveOrUpdate(Product product);
    /**
     * 商品上下架
     */
    ServerResponse set_sale_status(Integer productId,Integer status);
    /**
     * 查看商品的详情
     */
    ServerResponse detail(Integer productId);
    /**
     * 后台-分页查看商品列表
     */
    ServerResponse findAll(int pageNum,int pageSize);
    /**
     * 后台-商品搜索 分页
     */
    ServerResponse search(Integer productId,String productName,Integer pageNum, Integer pageSize);
    /**
     * 图片上传
     */
    ServerResponse upload(MultipartFile file,String path);

    /**
     * 前台-商品详细
     */
    ServerResponse detail_portal(Integer productId);
    /**
     *前台商品搜索
     */
    ServerResponse List(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy);
}
