package cn.tedu.straw.portal.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UpdateQuestionVo {

    private Integer id;

    @NotBlank(message = "标题不能为空")
    @Pattern(regexp = "^.{3,50}$", message = "标题格式不正确,3~50字符")
    private String title;

    @NotBlank(message = "问题内容不能为空!")
    private String content;
}
