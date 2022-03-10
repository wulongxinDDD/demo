package cn.tedu.straw.portal.vo;

import cn.tedu.straw.commons.model.Question;
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
 * @author tedu.cn
 * @since 2021-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class IndexQuestionVo extends Question implements Serializable {


    private Integer id;


    private String sendname;


    private LocalDateTime time;


    private String content;


    private String username;


    private String title;


    private String img;


    private Integer views;


    private Integer collect;


    private boolean isFriend=false;


    private boolean isCollection=false;


}
