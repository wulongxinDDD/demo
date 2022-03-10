package cn.tedu.straw.commons.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @since 2021-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("answers")
public class Answer implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 回答目标问题id
     */
    @TableField("question_id")
    private String questionId;

    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 创建时间
     */
    @TableField("time")
    private LocalDateTime time;

    /**
     * 图片
     */
    @TableField("img")
    private String img;

    /**
     * 头像
     */
    @TableField("head")
    private String head;

}
