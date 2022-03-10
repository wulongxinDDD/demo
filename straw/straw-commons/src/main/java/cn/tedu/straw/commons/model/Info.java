package cn.tedu.straw.commons.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2021-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("emp_info")
public class Info implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "emp_id", type = IdType.AUTO)
    private Integer empId;

    @TableField("emp_no")
    private String empNo;

    @TableField("emp_name")
    private String empName;

    @TableField("emp_sex")
    private String empSex;

    @TableField("emp_date")
    private LocalDate empDate;

    @TableField("emp_dept_id")
    private Integer empDeptId;

    @TableField("emp_deptpost_id")
    private Integer empDeptpostId;

    @TableField("emp_address")
    private String empAddress;

}
