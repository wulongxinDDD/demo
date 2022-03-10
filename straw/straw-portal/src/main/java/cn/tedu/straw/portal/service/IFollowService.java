package cn.tedu.straw.portal.service;

import cn.tedu.straw.portal.vo.UserVo;
import com.github.pagehelper.PageInfo;

public interface IFollowService {
    Boolean addFollow(String follow);

    Boolean selectFriendByQuestionId(Integer QuestionId);

    Boolean deleteFriendByUsername(String username);

    PageInfo<UserVo> selectFollow(Integer pageNum, Integer pageSize, String username);
}
