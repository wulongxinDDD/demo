package cn.tedu.straw.portal.mapper;


import cn.tedu.straw.commons.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author tedu.cn
 * @since 2021-01-13
 */
@Repository
public interface UserMapper extends BaseMapper<User> {


    //按用户名查询用户
    @Select("select * from usertable where username=#{username}")
    User findUserByUsername(String username);

    //修改用户信息
    @Update("update usertable set nickname=#{nickname},autograph=#{autograph},gender=#{gender},birthday=#{birthday} where username=#{username}")
    Integer updateUserByUsername(String nickname, String autograph, String gender, LocalDate birthday, String username);

    //修改用户头像
    @Update("update usertable set img=#{img} where username=#{username}")
    Integer updateUserImg(String img,String username);


}
