package cn.tedu.straw.portal.service.impl;

import cn.tedu.straw.commons.model.BlackList;
import cn.tedu.straw.commons.model.Friend;
import cn.tedu.straw.commons.model.User;
import cn.tedu.straw.portal.mapper.BlackListMapper;
import cn.tedu.straw.portal.mapper.FriendMapper;
import cn.tedu.straw.portal.service.IBlankService;
import cn.tedu.straw.portal.service.IUserService;
import cn.tedu.straw.sys.vo.FansVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class BlankServiceImpI implements IBlankService {
    @Resource
    BlackListMapper blackListMapper;
    @Resource
    IUserService userService;
    @Resource
    FriendMapper friendMapper;
    @Override
    public boolean addBlank(String bUsername) {
        String username = userService.currentUsername();
        QueryWrapper<BlackList> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username).eq("b_username",bUsername);
        Integer count = blackListMapper.selectCount(queryWrapper);
        if (count==0) {
            BlackList blackList = new BlackList();
            blackList.setUsername(username)
                    .setBUsername(bUsername);
            blackListMapper.insert(blackList);
            QueryWrapper<Friend> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("follow",bUsername).eq("fans",username);
            friendMapper.delete(queryWrapper1);
            QueryWrapper<Friend> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("follow",username).eq("fans",bUsername);
            friendMapper.delete(queryWrapper2);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public PageInfo<FansVo> selectBlank(Integer pageNum, Integer pageSize) {
        String username = userService.currentUsername();
        QueryWrapper<BlackList> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",username);
        PageHelper.startPage(pageNum,pageSize);
        List<BlackList> blackLists = blackListMapper.selectList(queryWrapper);
        List<FansVo> fansVoList = new ArrayList<>();
        for (BlackList list : blackLists){
            FansVo fansVo = new FansVo();
            fansVo.setUsername(list.getBUsername());
            User user = userService.getUserByUsername(list.getBUsername());
            fansVo.setNickname(user.getNickname());
            fansVoList.add(fansVo);
        }
        return new PageInfo<FansVo>(fansVoList);
    }

    @Override
    public boolean deleteBlank(String username) {
        QueryWrapper<BlackList> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("b_username",username);
        Integer a = blackListMapper.delete(queryWrapper);
        if (a==1){
            return true;
        }else {
            return false;
        }
    }
}
