package cn.tedu.straw.portal.service.impl;


import cn.tedu.straw.commons.model.User;
import cn.tedu.straw.commons.service.ServiceException;
import cn.tedu.straw.portal.mapper.FriendMapper;
import cn.tedu.straw.portal.mapper.QuestionMapper;
import cn.tedu.straw.portal.mapper.UserMapper;
import cn.tedu.straw.portal.service.IQuestionService;
import cn.tedu.straw.portal.service.IUserService;
import cn.tedu.straw.portal.vo.PersonalVo;
import cn.tedu.straw.portal.vo.RegisterVo;
import cn.tedu.straw.portal.vo.UpdateUserVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


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
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Override
    public void register(RegisterVo registerVo) {
        if (registerVo == null) {
            throw ServiceException.unprocesabelEntity("表单数据为空");
        }
        User u = userMapper.findUserByUsername(registerVo.getUsername());
        if (u != null) {
            throw ServiceException.unprocesabelEntity("手机号已经注册");
        }
        log.debug("用户名可用!");
        User user = new User();
        user.setUsername(registerVo.getUsername());
        user.setNickname(registerVo.getNickname());
        user.setPassword("{bcrypt}" + passwordEncoder.encode(registerVo.getPassword()));
        user.setImg("/img/user.jpg");
        int num = userMapper.insert(user);
        if (num != 1) {
            log.debug("异常!");
            log.debug(String.valueOf(num));
            throw ServiceException.busy();
        }
    }

    @Override
    public UserDetails getUserDetails(String username) {
        //根据用户名查询出用户对象
        User user = userMapper.findUserByUsername(username);
        if (user == null) {
            return null;
        }
        String[] auths = new String[1];
        auths[0] = "2";
        //构建UserDetails
        UserDetails u = org.springframework.security.core
                .userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(auths)
                //getLocked默认都是0,所以要判==1得到false表示不锁定
                .accountLocked(false)
                //getEnabled默认都是1,所以要判==0得到false表示可用
                .disabled(false)
                .build();
        return u;
    }

    @Override
    public User getUserByUsername(String username) {
        if (username == null){
            username = currentUsername();
        }
        return userMapper.findUserByUsername(username);
    }

    @Override
    public String currentUsername() {
        // 获得当前用户名
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw ServiceException.invalidRequest("没有登录!");
        }
        String username = authentication.getName();
        return username;
    }


    @Autowired
    IQuestionService questionService;
    @Autowired
    FriendMapper friendMapper;
    @Autowired
    QuestionMapper questionMapper;

    @Override
    public PersonalVo currentPersonalVo() {
        //获得当前登录用户的用户名
        String username = currentUsername();
        User u = userMapper.findUserByUsername(username);
        QueryWrapper query = new QueryWrapper();
        query.eq("follow", username);
        Integer fansCount = friendMapper.selectCount(query);
        QueryWrapper query1 = new QueryWrapper();
        query1.eq("fans", username);
        Integer followCount = friendMapper.selectCount(query1);
        QueryWrapper query2 = new QueryWrapper();
        query2.eq("username", username);
        Integer postCount = questionMapper.selectCount(query2);
        PersonalVo personalVo = new PersonalVo();
        personalVo.setNickname(u.getNickname())
                .setFollow(followCount)
                .setFans(fansCount)
                .setPost(postCount)
                .setUsername(username);
        return personalVo;
    }

    @Override
    public PersonalVo currentPersonalVoU(String username) {
        String now = currentUsername();
        if (username == null){
            username = now;
        }
        User u = userMapper.findUserByUsername(username);
        QueryWrapper query = new QueryWrapper();
        query.eq("follow", username);
        Integer fansCount = friendMapper.selectCount(query);
        QueryWrapper query1 = new QueryWrapper();
        query1.eq("fans", username);
        Integer followCount = friendMapper.selectCount(query1);
        QueryWrapper query2 = new QueryWrapper();
        query2.eq("username", username);
        Integer postCount = questionMapper.selectCount(query2);
        PersonalVo personalVo = new PersonalVo();
        personalVo.setNickname(u.getNickname())
                .setNow(now)
                .setUsername(username)
                .setFollow(followCount)
                .setFans(fansCount)
                .setPost(postCount);
        return personalVo;
    }

    @Override
    public boolean updateUser(UpdateUserVo updateUserVo, String username) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthday = LocalDate.parse(updateUserVo.getBirthday(),df);
        Integer a = userMapper.updateUserByUsername(updateUserVo.getNickname(),updateUserVo.getAutograph(),updateUserVo.getGender(),birthday,username);
        if (a==1){
            return true;
        }else {
            return false;
        }
    }

    @Value("${straw.resource.path}")
    private File resourcePath;

    @Value("${straw.resource.host}")
    private String resourceHost;

    @Override
    public boolean updateImg(MultipartFile imageFile) throws IOException {
        String username = currentUsername();
        String path = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now());
        //path:2021/01/20
        //实例化上传目标文件夹
        //folder:E:/upload/2021/01/20
        File folder = new File(resourcePath, path);
        folder.mkdirs();
        log.debug("上传的文件夹为:{}", folder.getAbsolutePath());
        //设计文件名随机生成
        //获取原始文件名
        String filename = imageFile.getOriginalFilename();
        //获取后缀名(扩展名)
        String ext = filename.substring(filename.lastIndexOf("."));// .png
        log.debug("文件名:{},后缀名:{}", filename, ext);

        //随机生成文件名
        //使用UUID来获得随机字符串
        String name = UUID.randomUUID().toString() + ext;
        //E:/upload/2021/01/20/uuid.png
        File file = new File(folder, name);

        log.debug("最终文件名:{}", file.getAbsolutePath());
        //写入文件
        imageFile.transferTo(file);
        //返回静态资源服务器访问该图片的路径
        String url = resourceHost + "/" + path + "/" + name;
        //E:/upload/a.png    ->  localhost:8899/a.png
        //E:/upload/2021/01/20/uuid.png
        //                   ->  localhost:8899/2021/01/20/uuid.png
        log.debug("访问路径为:{}", url);

        Integer a = userMapper.updateUserImg(url,username);

        return a==1;
    }
}
