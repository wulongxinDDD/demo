package cn.tedu.straw.portal.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class NewsVo {
    private String username;

    private String sendname;

    private String usernameU;

    private Integer questionId;

    private String content;

    private String title;
}
