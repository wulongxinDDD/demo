package cn.tedu.straw.portal.service;

import cn.tedu.straw.sys.vo.FansVo;
import com.github.pagehelper.PageInfo;

public interface IBlankService {
    boolean addBlank(String bUsername);

    PageInfo<FansVo> selectBlank(Integer pageNum, Integer pageSize);

    boolean deleteBlank(String username);
}
