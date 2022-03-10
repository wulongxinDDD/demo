package cn.tedu.straw.portal.controller;


import cn.tedu.straw.commons.model.Info;
import cn.tedu.straw.commons.vo.R;
import cn.tedu.straw.portal.service.IInfoService;
import cn.tedu.straw.portal.vo.InfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author tedu.cn
 * @since 2021-01-13
 */
@RestController
@RequestMapping("/v1/info")
@Slf4j
public class InfoController {

    @Autowired
    IInfoService iInfoServicel;


    @PostMapping
    public R<Info> info(@Validated InfoVo infoVo, BindingResult res) {
        if (res.hasErrors()) {
            String msg = res.getFieldError().getDefaultMessage();
            log.warn(msg);
            return R.unproecsableEntity(msg);
        }
        if (iInfoServicel.info(infoVo)){
            return R.ok("新增成功");
        }else {
            return R.invalidRequest("异常");
        }
    }


}
