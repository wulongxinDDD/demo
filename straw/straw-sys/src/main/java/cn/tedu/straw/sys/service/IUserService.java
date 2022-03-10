package cn.tedu.straw.sys.service;


import cn.tedu.straw.sys.vo.FansVo;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author tedu.cn
 * @since 2021-01-13
 */

public interface IUserService {

    PageInfo<FansVo> selectFans(String username, Integer pageNum, Integer pageSize,Boolean isMe);

}
