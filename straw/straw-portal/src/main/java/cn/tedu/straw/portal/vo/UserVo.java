package cn.tedu.straw.portal.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

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
public class UserVo implements Serializable {

    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别
     */
    private String gender;

    /**
     * 个性签名
     */
    private String autograph;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 头像
     */
    private String img;

    /**
     * me?
     */
    private Boolean isMe;

}
