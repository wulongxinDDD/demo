package cn.tedu.straw.commons.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("blacklist")
public class BlackList {
    @TableField("username")
    private String username;
    @TableField("b_username")
    private String bUsername;
}
