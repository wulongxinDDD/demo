package cn.tedu.straw.portal.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class AnswerVo implements Serializable {
    //javax.validation.constraints.NotNull;
    @NotNull(message = "必须包含问题编号")
    private String questionId;

    @NotBlank(message = "回复内容不能为空")
    private String content;

}
