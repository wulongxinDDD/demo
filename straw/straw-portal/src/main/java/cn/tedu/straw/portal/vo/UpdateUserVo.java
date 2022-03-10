package cn.tedu.straw.portal.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class UpdateUserVo implements Serializable {
    @NotBlank(message = "昵称不能为空")
    @Pattern(regexp = "^.{2,20}$", message = "昵称长度是2到20个字符")
    private String nickname;
    @NotBlank(message = "性别不能为空")
    private String gender;
    @NotBlank(message = "生日不能为空")
    private String birthday;
    @NotBlank(message = "签名不能为空")
    private String autograph;
}
