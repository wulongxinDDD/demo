package cn.tedu.straw.portal.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author tedu.cn
 * @since 2021-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class QuestionDetailVo implements Serializable {


    private Integer id;

    /**
     * 发送人
     */
    private String sendname;

    /**
     * 时间
     */
    private LocalDateTime time;

    /**
     * 内容
     */
    private String content;

    /**
     * 发送人用户名
     */
    private String username;

    /**
     * 标题
     */
    private String title;

    /**
     * 图片
     */
    private String img;

    /**
     * 浏览量
     */
    private Integer views;

    /**
     * 收藏量
     */
    private Integer collect;

    /**
     * 性别
     */
    private String gender;

    /**
     * 生日
     */
    private LocalDate birthday;
}
