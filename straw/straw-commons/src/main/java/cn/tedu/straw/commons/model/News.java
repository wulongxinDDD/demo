package cn.tedu.straw.commons.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("news")
public class News {
    @TableField("username")
    private String username;

    @TableField("username_u")
    private String usernameU;

    @TableField("question_id")
    private Integer questionId;

    @TableField("content")
    private String content;

    @TableField("title")
    private String title;
}
