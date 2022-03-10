package cn.tedu.straw.portal.controller;


import cn.tedu.straw.commons.model.Answer;
import cn.tedu.straw.commons.service.ServiceException;
import cn.tedu.straw.commons.vo.R;
import cn.tedu.straw.portal.service.IAnswerService;
import cn.tedu.straw.portal.vo.AnswerVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author tedu.cn
 * @since 2021-01-13
 */
@RestController
@RequestMapping("/v1/answers")
@Slf4j
public class AnswerController {

    @Autowired
    IAnswerService answerService;


    @PostMapping
    public R<Answer> postAnswer(@Validated AnswerVo answerVo, BindingResult res, @AuthenticationPrincipal UserDetails user) {
        if (res.hasErrors()) {
            String msg = res.getFieldError().getDefaultMessage();
            log.warn(msg);
            return R.unproecsableEntity(msg);
        }
        log.debug("开始新增评论:{}", answerVo);
        try {

            answerService.saveAnswer(answerVo, user.getUsername());
            return R.ok("评论新增成功");
        }catch (ServiceException e){
            return R.failed(e);
        }
    }

    //根据问题id查询所有评论的方法
    @GetMapping("/question/{id}")
    public R<List<Answer>> questionAnswers(@PathVariable String id) {
        if (id == null) {
            return R.notFound("ID不存在");
        }
        List<Answer> answers = answerService.getAnswersByQuestionId(id);
        log.debug("answers");
        log.debug(answers.toString());
        return R.ok(answers);

    }

}
