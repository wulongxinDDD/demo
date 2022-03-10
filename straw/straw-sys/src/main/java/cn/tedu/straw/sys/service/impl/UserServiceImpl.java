package cn.tedu.straw.sys.service.impl;


import cn.tedu.straw.commons.model.Friend;
import cn.tedu.straw.commons.model.User;
import cn.tedu.straw.commons.service.ServiceException;
import cn.tedu.straw.sys.mapper.FriendMapper;
import cn.tedu.straw.sys.mapper.UserMapper;
import cn.tedu.straw.sys.service.IUserService;
import cn.tedu.straw.sys.vo.FansVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author tedu.cn
 * @since 2021-01-13
 */
@Service
@Slf4j
public class UserServiceImpl implements IUserService  {
    @Autowired
    FriendMapper friendMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public PageInfo<FansVo> selectFans(String username, Integer pageNum, Integer pageSize,Boolean isMe) {
        if (pageNum == null || pageSize == null) {
            throw ServiceException.invalidRequest("缺失分页参数!");
        }
        QueryWrapper<Friend> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("follow",username);
        List<Friend> list = friendMapper.selectList(queryWrapper);

        QueryWrapper<Friend> friendQueryWrapper = new QueryWrapper<>();
        friendQueryWrapper.eq("fans",username);
        List<Friend> friendList = friendMapper.selectList(friendQueryWrapper);

        List<FansVo> fansVoList = new ArrayList<>();
        for (Friend fans : list){
            FansVo fansVo = new FansVo();
            String user = fans.getFans();
            fansVo.setUsername(user);
            User use = userMapper.selectUser(user);
            fansVo.setNickname(use.getNickname());
            fansVo.setAutograph(use.getAutograph());
            fansVo.setBirthday(use.getBirthday());
            fansVo.setGender(use.getGender());
            fansVo.setImg(use.getImg());
            fansVo.setMe(isMe);
            A: for (Friend friend:friendList){
                if (friend.getFollow().equals(fans.getFans())){
                    fansVo.setFriend(true);
                    break A;
                }
            }
            fansVoList.add(fansVo);
        }
        PageInfo<FansVo> page = getPageInfo(pageNum,pageSize,fansVoList);
        return page;
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
