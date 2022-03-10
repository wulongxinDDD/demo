package cn.tedu.straw.portal.mapper;

import cn.tedu.straw.commons.model.BlackList;
import cn.tedu.straw.commons.model.Friend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendMapper extends BaseMapper<Friend> {
    @Select("SELECT * FROM friend where follow=#{follow} and fans=#{fans}")
    List<Friend> findFriend(String follow,String fans);
}
