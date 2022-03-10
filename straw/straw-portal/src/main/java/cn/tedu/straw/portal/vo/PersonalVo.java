package cn.tedu.straw.portal.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class PersonalVo {
    private String now;

    private String username;

    private String nickname;

    private Integer follow;

    private Integer fans;

    private Integer post;

}
