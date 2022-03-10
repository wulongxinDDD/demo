package cn.tedu.straw.portal.service.impl;

import cn.tedu.straw.commons.model.Friend;
import cn.tedu.straw.commons.model.News;
import cn.tedu.straw.commons.model.Question;
import cn.tedu.straw.commons.model.User;
import cn.tedu.straw.commons.service.ServiceException;
import cn.tedu.straw.portal.mapper.FriendMapper;
import cn.tedu.straw.portal.mapper.NewsMapper;
import cn.tedu.straw.portal.mapper.QuestionMapper;
import cn.tedu.straw.portal.mapper.UserMapper;
import cn.tedu.straw.portal.service.IFollowService;
import cn.tedu.straw.portal.service.IUserService;
import cn.tedu.straw.portal.vo.UserVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FollowServiceImpl  implements IFollowService {
    @Autowired
    FriendMapper friendMapper;
    @Autowired
    IUserService userService;
    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    NewsMapper newsMapper;

    @Override
    public Boolean addFollow(String follow) {
        String fans = userService.currentUsername();
        List<Friend> list = friendMapper.findFriend(follow,fans);
        User user = userMapper.findUserByUsername(fans);
        if (list.size()<=0) {
            Friend friend = new Friend();
            friend.setFollow(follow);
            friend.setFans(fans);
            int a = friendMapper.insert(friend);
            if (a == 1) {
                News news = new News()
                        .setUsername(follow)
                        .setUsernameU(fans);
                newsMapper.insert(news);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public Boolean selectFriendByQuestionId(Integer questionId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw ServiceException.invalidRequest("没有登录!");
        }
        String fans = authentication.getName();
        Question question = questionMapper.selectById(questionId);
        String follow = question.getUsername();
        QueryWrapper<Friend> queryWrapper = new QueryWrapper();
        queryWrapper.eq("follow",follow);
        queryWrapper.eq("fans",fans);
        int count = friendMapper.selectCount(queryWrapper);
        log.debug("count:{}",count);
        if (count == 1){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Boolean deleteFriendByUsername(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw ServiceException.invalidRequest("没有登录!");
        }
        String name = authentication.getName();
        QueryWrapper<Friend> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("follow",username);
        queryWrapper.eq("fans",name);
        int a = friendMapper.delete(queryWrapper);
        if (a==1){
            return true;
        }else {
            return false;
        }
    }


    @Override
    public PageInfo<UserVo> selectFollow(Integer pageNum, Integer pageSize , String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        Boolean isMe = name.equals(username);
        QueryWrapper<Friend> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("fans",username);
        List<Friend> list = friendMapper.selectList(queryWrapper);
        List<UserVo> userVoList = new ArrayList<>();
        for (Friend friend:list){
            System.out.println("---------------");
            System.out.println(friend.getFollow());
             User user = userMapper.findUserByUsername(friend.getFollow());
             UserVo userVo = new UserVo()
                     .setAutograph(user.getAutograph())
                     .setBirthday(user.getBirthday())
                     .setGender(user.getGender())
                     .setImg(user.getImg())
                     .setUsername(user.getUsername())
                     .setId(user.getId())
                     .setNickname(user.getNickname())
                     .setIsMe(isMe);
             userVoList.add(userVo);
        }

        return getPageInfo(pageNum,pageSize,userVoList);

    }

    public static <T> PageInfo<T> getPageInfo(int currentPage, int pageSize, List<T> list) {
        int total = list.size();
        if (total > pageSize) {
            int toIndex = pageSize * currentPage;
            if (toIndex > total) {
                toIndex = total;
            }
            int totalPage = total % pageSize == 0 ? (total/pageSize) : (total/pageSize)+1;
            if (totalPage < currentPage){
                list = new ArrayList<>();
            }else {
                list = list.subList(pageSize * (currentPage - 1), toIndex);
            }
        }else {
            list = currentPage == 1 ? list : new ArrayList<>();
        }
        Page<T> page = new Page<>(currentPage, pageSize);
        page.addAll(list);
        page.setPages((total + pageSize - 1) / pageSize);
        page.setTotal(total);

        return new PageInfo<>(page);
    }
}
