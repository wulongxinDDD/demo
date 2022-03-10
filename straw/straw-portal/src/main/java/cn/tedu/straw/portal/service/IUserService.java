package cn.tedu.straw.portal.service;


import cn.tedu.straw.commons.model.User;
import cn.tedu.straw.portal.vo.PersonalVo;
import cn.tedu.straw.portal.vo.RegisterVo;
import cn.tedu.straw.portal.vo.UpdateUserVo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author tedu.cn
 * @since 2021-01-13
 */
public interface IUserService  {


    //根据用户名获得用户认证信息的业务逻辑层方法
    UserDetails getUserDetails(String username);

    void register(RegisterVo registerVo);

    //根据姓名获得用户对象
    User getUserByUsername(String username);

    //获得当前登录用户的方法
    String currentUsername();

    //查询用户信息面板的方法
    PersonalVo currentPersonalVo();

    //查询主题发起人面板
    PersonalVo currentPersonalVoU(String username);

    //修改用户信息
    boolean updateUser(UpdateUserVo updateUserVo, String username);

    //修改头像
    boolean updateImg(MultipartFile imageFile) throws IOException;
}
