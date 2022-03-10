package cn.tedu.straw.portal.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
public class InfoVo implements Serializable {

    private String empNo;

    private String empName;

    private String empSex;

    private String empDate;

    private String empDeptName;

    private String empDeptpostName;

    private String empAddress;

}
