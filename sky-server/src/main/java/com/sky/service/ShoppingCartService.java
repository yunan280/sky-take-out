package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查看购物车
     * @return 当前用户的购物车列表
     */
    List<ShoppingCart> listShoppingCart();

    /**
     * 清空购物车
     */
    void cleanShoppingCart();
}
