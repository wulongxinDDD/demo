package cn.tedu.straw.sys.controller;


import cn.tedu.straw.sys.service.IUserService;
import cn.tedu.straw.sys.vo.FansVo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/selectFans")
    public PageInfo<FansVo> selectFans(String username,Integer pageNum,Integer pageSize,Boolean isMe){
        PageInfo<FansVo> list = userService.selectFans(username,pageNum,pageSize,isMe);
        return list;
    }


}
