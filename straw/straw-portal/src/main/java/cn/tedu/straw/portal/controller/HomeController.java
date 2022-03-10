package cn.tedu.straw.portal.controller;

import cn.tedu.straw.commons.service.ServiceException;
import cn.tedu.straw.portal.service.IUserService;
import cn.tedu.straw.portal.vo.R;
import cn.tedu.straw.portal.vo.RegisterVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Slf4j
public class HomeController {
    @Autowired
    IUserService userService;

    @GetMapping("/register.html")
    public ModelAndView register() {
        return new ModelAndView ("register");
    }

    @GetMapping("/info.html")
    public ModelAndView info() {
        return new ModelAndView ("info");
    }

    @GetMapping("/login.html")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @GetMapping("/index.html")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @GetMapping("/create.html")
    public ModelAndView create() {
        return new ModelAndView("create");
    }

    @GetMapping("/fans.html")
    public ModelAndView fans() {
        return new ModelAndView("fans");
    }

    @GetMapping("/my.html")
    public ModelAndView my() {
        return new ModelAndView("my");
    }

    @GetMapping("/blank.html")
    public ModelAndView blank() {
        return new ModelAndView("blank");
    }

    @GetMapping("/follow.html")
    public ModelAndView follow() {
        return new ModelAndView("follow");
    }

    @GetMapping("/userInfo.html")
    public ModelAndView userinfo() {
        return new ModelAndView("userInfo");
    }

    @GetMapping("/user.html")
    public ModelAndView user() {
        return new ModelAndView("user");
    }

    @GetMapping("/updateImg.html")
    public ModelAndView updateImg() {
        return new ModelAndView("updateImg");
    }

    @GetMapping("/question/detail.html")
    public ModelAndView detail() {
        return new ModelAndView("question/detail");
    }

    @GetMapping("/question/update.html")
    public ModelAndView update(){
        return new ModelAndView("question/update");
    }

    //执行注册业务的控制器方法
    @PostMapping("/register")
    public R registerStudent(@Validated RegisterVo registerVo, BindingResult res) {
        log.debug("开始注册");
        if (res.hasErrors()) {
            String error = res.getFieldError().getDefaultMessage();
            return R.unproecsableEntity(error);
        }
        if (!registerVo.getPassword().equals(registerVo.getConfirm())) {
            return R.unproecsableEntity("两次密码输入不一致");
        }
        log.debug("得到表单信息:{}", registerVo);
        try {
            userService.register(registerVo);
            return R.created("注册成功!");
        } catch (ServiceException e) {
            return R.failed(e);
        }
    }
}
