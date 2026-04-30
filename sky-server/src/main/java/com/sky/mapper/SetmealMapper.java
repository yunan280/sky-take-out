package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     *
     * @param categoryId 分类id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 根据菜品id列表查询套餐菜品关系数量
     * @param dishIds
     * @return
     */
    @Select({
            "<script>",
            "select count(id) from setmeal_dish where dish_id in",
            "<foreach collection='dishIds' item='dishId' open='(' separator=',' close=')'>",
            "#{dishId}",
            "</foreach>",
            "</script>"
    })
    Integer countByDishIds(@Param("dishIds") List<Long> dishIds);

    /**
     * 新增套餐
     *
     * @param setmeal 套餐实体
     */
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * 套餐分页查询
     *
     * @param setmealPageQueryDTO 查询条件
     * @return 分页结果
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据id查询套餐
     *
     * @param id 套餐id
     * @return 套餐
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    /**
     * 根据id删除套餐
     *
     * @param id 套餐id
     */
    @Delete("delete from setmeal where id = #{id}")
    void deleteById(Long id);

    /**
     * 动态修改套餐
     *
     * @param setmeal 套餐实体
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);
}
