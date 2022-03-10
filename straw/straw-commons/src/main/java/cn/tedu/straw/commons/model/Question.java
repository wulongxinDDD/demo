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
@TableName("main")
public class Question implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 时间
     */
    @TableField("time")
    private LocalDateTime time;
    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * 发送人用户名
     */
    @TableField("username")
    private String username;
    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 图片
     */
    @TableField("img")
    private String img;

    /**
     * 浏览量
     */
    @TableField("views")
    private Integer views;

    /**
     * 收藏量
     */
    @TableField("collect")
    private Integer collect;
}
