package cn.tedu.straw.commons.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
@TableName("deptpost_info")
public class DeptpostInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "deptpost_id", type = IdType.AUTO)
    private Integer deptPostId;

    @TableField("deptpost_name")
    private String deptPostName;

    @TableField("dept_id")
    private String deptId;

}
