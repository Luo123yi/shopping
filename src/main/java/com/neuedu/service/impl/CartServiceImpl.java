package com.neuedu.service.impl;

import com.google.common.collect.Lists;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CartMapper;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Cart;
import com.neuedu.pojo.Product;
import com.neuedu.service.ICartService;
import com.neuedu.utils.BigDecimalUtils;
import com.neuedu.vo.CartProductVO;
import com.neuedu.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    CartMapper cartMapper;
    @Autowired
    ProductMapper productMapper;

    /**
     * 购物车中添加商品
     */
    @Override
    public ServerResponse add(Integer userId,Integer productId, Integer count) {
        //step1：参数的非空校验
        if (productId==null||count==null){
            return ServerResponse.createServerResponseByError("参数不能为空");
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product==null){
            return ServerResponse.createServerResponseByError("要添加的商品不存在");
        }
        //step2：根据productId和userId查询购物信息
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart==null){
            //添加
            Cart cart1 = new Cart();
            cart1.setUserId(userId);
            cart1.setProductId(productId);
            cart1.setQuantity(count);
            cart1.setChecked(Const.CartCheckedEnum.PRODUCT_CHECKED.getCode());
            cartMapper.insert(cart1);
        }else {
            //更新
            Cart cart1 = new Cart();
            cart1.setUserId(userId);
            cart1.setProductId(productId);
            cart1.setQuantity(count);
            cart1.setChecked(cart.getChecked());
            cart1.setId(cart.getId());
            cartMapper.updateByPrimaryKey(cart1);
        }
        CartVO cartVO = getCartVOLimit(userId);
        return ServerResponse.createServerResponseBySuccess(null,cartVO);
    }

    /**
     *购物车列表
     */
    @Override
    public ServerResponse list(Integer userId) {
        CartVO cartVO = getCartVOLimit(userId);
        return ServerResponse.createServerResponseBySuccess(null,cartVO);
    }

    /**
     * 更新购物车某个商品数量
     */
    @Override
    public ServerResponse update(Integer userId,Integer productId, Integer count) {
        //step1：参数判定
        if (productId==null||count==null){
            return ServerResponse.createServerResponseByError("参数不能为空");
        }
        //step2：查询购物车中商品
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId,productId);
        if (cart!=null){
            //step3：更新数量
            cart.setQuantity(count);
            int update = cartMapper.updateByPrimaryKey(cart);
            if (update>0){
                return ServerResponse.createServerResponseBySuccess("更新成功",getCartVOLimit(userId));
            }else{
                return ServerResponse.createServerResponseBySuccess("更新失败",getCartVOLimit(userId));
            }
        }
        return ServerResponse.createServerResponseByError("更新失败,商品不存在");

    }

    /**
     * 移除购物车中某个产品
     */
    @Override
    public ServerResponse delete_product(Integer userId, String productIds) {
        //step1：参数校验
        if (productIds==null&&productIds.equals("")){
            return ServerResponse.createServerResponseByError("参数不能为空");
        }
        //step2:productIds--> List<Integer>
        List<Integer> productIdList = Lists.newArrayList();
        String[] productIdsArr = productIds.split(",");
        if (productIdsArr!=null&&productIdsArr.length>0){
            for (String productIdstr:productIdsArr){
                Integer productId=Integer.parseInt(productIdstr);
                productIdList.add(productId);
            }
        }
        //step3:调用dao
        cartMapper.deleteByUserIdAndProductIds(userId,productIdList);
        //step4：返回结果
        return ServerResponse.createServerResponseBySuccess(null,getCartVOLimit(userId));
    }

    /**
     * 购物车中选中某个商品,取消选中，全选，取消全选
     */
    @Override
    public ServerResponse select(Integer userId, Integer productId,Integer check) {
        //step1：非空校验
//        if (productId==null){
//            return ServerResponse.createServerResponseByError("参数不能为空");
//        }
        //step2：dao接口
         cartMapper.selectOrUnselectProduct(userId,productId,check);
        //step3：返回结果
        return ServerResponse.createServerResponseBySuccess(null,getCartVOLimit(userId));
    }

    /**
     * 购物车中产品数量
     */
    @Override
    public ServerResponse get_cart_product_count(Integer userId) {
        int quantity = cartMapper.get_cart_product_count(userId);
        return ServerResponse.createServerResponseBySuccess(null,quantity);
    }

    /**
     *封装高复用对象
     */
    private CartVO getCartVOLimit(Integer userId){
        CartVO cartVO = new CartVO();
        //step1：根据userId查询购物信息--》List<Cart>
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        //step2：List<Cart>-->List<CartProductVO>
        List<CartProductVO> cartProductVOList = Lists.newArrayList();
        //购物车总价格
        BigDecimal carttotalprice=new BigDecimal("0");
        if (cartList!=null||cartList.size()>0){
            for (Cart cart:cartList){
                CartProductVO cartProductVO = new CartProductVO();
                cartProductVO.setId(cart.getId());
                cartProductVO.setQuantity(cart.getQuantity());
                cartProductVO.setUserId(cart.getUserId());
                cartProductVO.setProductChecked(cart.getChecked());
                //查询商品
                Product product = productMapper.selectByPrimaryKey(cart.getProductId());
                if (product!=null){
                    cartProductVO.setProductId(cart.getProductId());
                    cartProductVO.setProductMainImage(product.getMainImage());
                    cartProductVO.setProductName(product.getName());
                    cartProductVO.setProductPrice(product.getPrice());
                    cartProductVO.setProductStatus(product.getStatus());
                    cartProductVO.setProductStock(product.getStock());
                    cartProductVO.setProductSubtitle(product.getSubtitle());
                    int stock = product.getStock();
                    int limitProductCount=0;
                    if (stock>=cart.getQuantity()){
                        limitProductCount=cart.getQuantity();
                        cartProductVO.setLimitQuantity("LIMIT_NUM_SUCCESS");
                    }else {
                        //商品库存不足
                        limitProductCount=stock;
                        //更新购物车中商品的数量
                        Cart cart1 = new Cart();
                        cart1.setId(cart.getId());
                        cart1.setQuantity(stock);
                        cart1.setProductId(cart.getProductId());
                        cart1.setChecked(cart.getChecked());
                        cart1.setUserId(userId);
                        cartMapper.updateByPrimaryKey(cart1);
                        cartProductVO.setLimitQuantity("LIMIT_NUM_FAIL");
                    }
                    cartProductVO.setQuantity(limitProductCount);
                    cartProductVO.setProductTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(),
                            Double.valueOf(cartProductVO.getQuantity())));
                }

                //判断商品是否被选中
                if (cartProductVO.getProductChecked()==Const.CartCheckedEnum.PRODUCT_CHECKED.getCode()){
                   //被选中商品
                    carttotalprice = BigDecimalUtils.add(carttotalprice.doubleValue(), cartProductVO.getProductTotalPrice().doubleValue());
                }
                cartProductVOList.add(cartProductVO);
            }
        }
        cartVO.setCartProductVOList(cartProductVOList);
        //step3:计算总价格
        cartVO.setCarttotalprice(carttotalprice);
        //step4:判断购物车是否全选
        int count = cartMapper.isCheckedAll(userId);
        if (count>0){
            cartVO.setIsallchecked(false);
        }else {
            cartVO.setIsallchecked(true);
        }
        //step5：返回结果
        return cartVO;
    }
}
