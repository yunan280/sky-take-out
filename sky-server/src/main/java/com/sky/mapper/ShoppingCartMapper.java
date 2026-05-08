package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
     * 动态条件查询
     * @param shoppingCart 查询条件（如用户id、菜品id等）
     * @return 购物车列表
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 根据id修改购物车数量
     * @param shoppingCart 含id与更新后的数量
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumberById(ShoppingCart shoppingCart);

    /**
     * 插入购物车数据
     * @param shoppingCart 购物车实体
     */
    @Insert("insert into shopping_cart (name, user_id, dish_id,setmeal_id, dish_flavor,number,amount,image,create_time) " +
            "values (#{name}, #{userId}, #{dishId},#{setmealId}, #{dishFlavor},#{number},#{amount},#{image},#{createTime})")
    void insert(ShoppingCart shoppingCart);

    /**
     * 根据用户id清空购物车
     * @param userId 用户id
     */
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void deleteByUserId(Long userId);
}