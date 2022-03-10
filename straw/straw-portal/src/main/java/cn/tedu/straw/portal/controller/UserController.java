package cn.tedu.straw.portal.controller;


import cn.tedu.straw.commons.model.User;
import cn.tedu.straw.portal.service.*;
import cn.tedu.straw.portal.vo.*;
import cn.tedu.straw.sys.vo.FansVo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author tedu.cn
 * @since 2021-01-13
 */
@RestController
@RequestMapping("/v1/users")
@Slf4j
public class UserController {
    @Autowired
    IUserService userService;
    @Autowired
    IQuestionService questionService;

    @GetMapping("/table")
    public R<PersonalVo> table() {
        PersonalVo personalVo = userService.currentPersonalVo();
        log.debug(personalVo.getNickname());
        return R.ok(personalVo);
    }

    @GetMapping("/table/{id}")
    public R<PersonalVo> tableU(@PathVariable Integer id) {
        log.debug("执行了");
        if (id == null)
            return R.invalidRequest("ID不能为空!");
        QuestionDetailVo question = questionService.getQuestionById(id);
        String username = question.getUsername();
        PersonalVo personalVo = userService.currentPersonalVoU(username);
        return R.ok(personalVo);
    }

    @GetMapping("/table/byUsername/{username}")
    public R<PersonalVo> tableByUsername(@PathVariable String username) {
        PersonalVo personalVo = userService.currentPersonalVoU(username);
        return R.ok(personalVo);
    }

    @GetMapping("/me")
    public R<User> me(@AuthenticationPrincipal UserDetails users) {
        String username = users.getUsername();
        User user = userService.getUserByUsername(username);
        log.debug(user.getUsername());
        return R.ok(user);
    }

    @PostMapping("/updateUser")
    public R updateUser(@Validated UpdateUserVo updateUserVo, BindingResult result){
        if (result.hasErrors()){
            String error = result.getFieldError().getDefaultMessage();
            return R.unproecsableEntity(error);
        }
        String username = userService.currentUsername();
        boolean a = userService.updateUser(updateUserVo,username);
        if (a){
            return R.ok("修改成功");
        }else {
            return R.invalidRequest("异常");
        }
    }

    @Value("${straw.resource.path}")
    private File resourcePath;

    @Value("${straw.resource.host}")
    private String resourceHost;

    //稻草项目上传富文本编辑器中选中图片的方法
    @PostMapping("/upload/image")
    public R<String> uploadImage(MultipartFile imageFile) throws IOException {
        //文件夹按年\月\日来分割
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
        return R.ok(url);
    }

    @PostMapping("/update/img")
    public String updateImg(MultipartFile imageFile){
        if (imageFile == null){
            return "请选择图片";
        }
        boolean a=false;
        try {
            a = userService.updateImg(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (a){
            return "修改头像成功";
        }else {
            return "修改头像失败";
        }
    }

    @Autowired
    IFollowService followService;

    @PostMapping("/{follow}")
    public R addFollow(@PathVariable String follow) {
        if (follow == null) {
            return R.invalidRequest("关注人用户名不能为空");
        }
        log.debug(follow);
        boolean a = followService.addFollow(follow);
        if (a) {
            return R.ok("添加关注成功");
        }else {
            return R.invalidRequest("已经关注过了哦");
        }
    }

    @GetMapping("/selectFriendByQuestionId/{questionId}")
    public R selectFriendByQuestionId(@PathVariable Integer questionId){
        if (questionId == null){
            return R.invalidRequest("问题ID不能为空");
        }
        boolean a = followService.selectFriendByQuestionId(questionId);
        if (a){
            return R.ok("已经关注");
        }else {
            return R.invalidRequest("未关注");
        }

    }

    @GetMapping("/selectCollectByQuestionId/{questionId}")
    private R selectCollectByQuestionId(@PathVariable Integer questionId){
        if (questionId == null){
            return R.invalidRequest("问题ID不能为空");
        }
        boolean a = collectionService.selectCollectByQuestionId(questionId);
        if (a){
            return R.ok("已经收藏");
        }else {
            return R.invalidRequest("未收藏");
        }
    }

    @Resource
    IBlankService blankService;

    @PostMapping("/blank/{username}")
    public R addBlank(@PathVariable String username) {
        if (username == null){
            return R.invalidRequest("拉黑用户名不能为空");
        }
        log.debug(username);
        boolean a = blankService.addBlank(username);
        if (a) {
            return R.ok("拉黑成功");
        } else {
            return R.invalidRequest("已经拉黑过了哦");
        }
    }

    @Resource
    ICollectionService collectionService;

    @PostMapping("/addCollect/{questionId}")
    public R addCollect(@PathVariable Integer questionId){
        if (questionId == null){
            return R.invalidRequest("问题ID不能为空");
        }
        boolean a = collectionService.addCollection(questionId);
        if (a){
            return R.ok("收藏成功");
        }else {
            return R.invalidRequest("已经收藏过了");
        }

    }

    @PostMapping("/deleteFriend/{username}")
    public R deleteFriend(@PathVariable String username){
        if (username == null){
            return R.invalidRequest("对象用户名不能为空");
        }
        Boolean a = followService.deleteFriendByUsername(username);
        if (a){
            return R.ok("取消关注成功");
        }else {
            return R.invalidRequest("异常");
        }
    }

    @PostMapping("/deleteCollect/{questionId}")
    public R deleteCollect(@PathVariable Integer questionId){
        if (questionId == null){
            return R.invalidRequest("问题ID不能为空");
        }
        Boolean a = collectionService.deleteCollectByQuestionId(questionId);
        if (a){
            return R.ok("移除收藏成功");
        }else {
            return R.invalidRequest("异常");
        }
    }
    @Resource
    RestTemplate restTemplate;

    @GetMapping("/selectFans")
    public R<PageInfo<FansVo>> selectFans(Integer pageNum,String username){
        if (pageNum == null) {
            pageNum = 1;
        }
        Integer pageSize = 8;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String me = authentication.getName();
        boolean isMe = me.equals(username);
        String url= "http://sys-service/v1/users/selectFans?username={1}&pageNum={2}&pageSize={3}&isMe={4}";
        PageInfo fans = restTemplate.getForObject(url,PageInfo.class,username,pageNum,pageSize,isMe);
        log.debug("粉丝为:{}",fans);

        return R.ok(fans);
    }

    @GetMapping("/selectFollow")
    public R<PageInfo<UserVo>> selectFollow(Integer pageNum,String username){
        if (pageNum == null){
            pageNum = 1;
        }
        Integer pageSize = 8;
        PageInfo<UserVo> pageInfo = followService.selectFollow(pageNum,pageSize,username);
        return R.ok(pageInfo);
    }

    @GetMapping("/selectBlank")
    public R<PageInfo<FansVo>> selectBlank(Integer pageNum){
        if (pageNum == null) {
            pageNum = 1;
        }
        Integer pageSize = 8;
        PageInfo<FansVo> list = blankService.selectBlank(pageNum,pageSize);
        return R.ok(list);

    }

    @PostMapping("/deleteBlank/{username}")
    public R deleteBlank(@PathVariable String username){
        if (username == null){
            return R.invalidRequest("用户名不能为空");
        }
        Boolean a = blankService.deleteBlank(username);
        if (a){
            return R.ok("移除黑名单成功");
        }else {
            return R.invalidRequest("异常");
        }
    }


}
