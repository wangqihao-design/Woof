package org.lls.woof.woofauth.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.lls.woof.woofauth.entity.dto.User;

@Mapper
public interface UserDao extends BaseMapper<User> {

    @Select("SELECT * FROM user where username = #{username}")
    User getByUsername(String username);
}
