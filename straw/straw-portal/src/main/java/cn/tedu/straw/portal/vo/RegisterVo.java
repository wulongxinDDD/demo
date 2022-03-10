package cn.tedu.straw.portal.vo;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class RegisterVo implements Serializable {

    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^\\w{6,20}$", message = "用户名是6到20个字母")
    private String username;
    @NotBlank(message = "昵称不能为空")
    @Pattern(regexp = "^.{2,20}$", message = "昵称长度是2到20个字符")
    private String nickname;
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^\\w{6,20}$", message = "密码是6到20个字母,数字,_组成")
    private String password;
    @NotBlank(message = "确认密码不能为空")
    private String confirm;

}
