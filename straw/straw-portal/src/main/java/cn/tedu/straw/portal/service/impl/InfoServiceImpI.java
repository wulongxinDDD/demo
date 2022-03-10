package cn.tedu.straw.portal.service.impl;

import cn.tedu.straw.commons.model.DeptInfo;
import cn.tedu.straw.commons.model.DeptpostInfo;
import cn.tedu.straw.commons.model.Info;
import cn.tedu.straw.portal.mapper.DeptInfoMapper;
import cn.tedu.straw.portal.mapper.DeptPostInfoMapper;
import cn.tedu.straw.portal.mapper.InfoMapper;
import cn.tedu.straw.portal.service.IInfoService;
import cn.tedu.straw.portal.vo.InfoVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Wu
 * @date 2021-07-12 15:54
 */
@Service
@Slf4j
public class InfoServiceImpI implements IInfoService{
    @Autowired
    InfoMapper infoMapper;
    @Autowired
    DeptInfoMapper deptInfoMapper;
    @Autowired
    DeptPostInfoMapper deptPostInfoMapper;
    @Override
    public boolean info(InfoVo infoVo) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(infoVo.getEmpDate(), fmt);
        Info info = new Info();
        info.setEmpAddress(infoVo.getEmpAddress());
        info.setEmpDate(date);
        info.setEmpName(infoVo.getEmpName());
        info.setEmpSex(infoVo.getEmpSex());
        info.setEmpNo(infoVo.getEmpNo());
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("dept_name",infoVo.getEmpDeptName());
        DeptInfo deptInfo1 = deptInfoMapper.selectOne(queryWrapper1);
        info.setEmpDeptId(deptInfo1.getDeptId());
        QueryWrapper queryWrapper2 = new QueryWrapper();
        queryWrapper2.eq("deptpost_name",infoVo.getEmpDeptpostName());
        DeptpostInfo deptInfo2 = deptPostInfoMapper.selectOne(queryWrapper2);
        info.setEmpDeptpostId(deptInfo2.getDeptPostId());
        int a = infoMapper.insert(info);
        if (a==1){
            return true;
        }else {
            return false;
        }
    }
}
