package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {

    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);

    void insert(User user);
    @Select("select * from user where id = #{id}")
    User getById(Long userId);

    /**
     * 统计总用户和新用户
     * @param map
     * @return
     */
    Integer sumByMap(Map map);
}
