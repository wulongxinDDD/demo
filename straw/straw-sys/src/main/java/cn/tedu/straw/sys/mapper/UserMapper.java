package cn.tedu.straw.sys.mapper;

import cn.tedu.straw.commons.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from usertable where username = #{username}")
    User selectUser(String username);
}
