package cn.tedu.straw.portal.controller;


import cn.tedu.straw.commons.model.Question;
import cn.tedu.straw.commons.service.ServiceException;
import cn.tedu.straw.portal.mapper.QuestionMapper;
import cn.tedu.straw.portal.mapper.UserMapper;
import cn.tedu.straw.portal.service.IQuestionService;
import cn.tedu.straw.portal.vo.*;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/v1/questions")
@Slf4j
public class QuestionController {

    @Autowired
    IQuestionService questionService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionMapper questionMapper;


    @GetMapping("/my")
    public R<PageInfo<Question>> my(Integer pageNum) {
        if (pageNum == null) {
            pageNum = 1;
        }
        Integer pageSize = 8;
        log.debug("开始查询帖子by当前用户");
        try {
            PageInfo<IndexQuestionVo> list = questionService.getMyQuestions(pageNum, pageSize);
            return R.ok(list);
        } catch (ServiceException e) {
            return R.failed(e);
        }
    }

    @PostMapping
    public R createQuestion(@Validated QuestionVo questionVo, BindingResult res) {
        if (res.hasErrors()) {
            String msg = res.getFieldError().getDefaultMessage();
            return R.unproecsableEntity(msg);
        }
        log.debug("准备执行新增:{}", questionVo);
        try {
            questionService.saveQuestion(questionVo);
            return R.ok("提交成功!");
        } catch (ServiceException e) {
            return R.failed(e);
        }
    }

    @GetMapping("/{id}")
    public R<QuestionDetailVo> question(@PathVariable Integer id) {
        log.debug("执行了");
        if (id == null)
            return R.invalidRequest("ID不能为空!");
        int o = questionMapper.updateViews(id);
        if (o!=1)
            throw ServiceException.invalidRequest("访问量修改增加异常!");
        QuestionDetailVo question = questionService.getQuestionById(id);
        return R.ok(question);
    }

    @PostMapping("/delete/{id}")
    public R<Question> deleteQuestion(@PathVariable Integer id){
        if (id == null)
            return R.invalidRequest("ID不能为空");
        int o = questionService.deleteQuestionById(id);
        if (o==1){
            return R.ok("删除成功");
        }else {
            return R.invalidRequest("删除失败");
        }

    }

    @PostMapping("/update/{id}")
    public R<Question> updateQuestion(@PathVariable Integer id,@Validated QuestionVo QuestionVo, BindingResult res){
        if (id == null)
            return R.invalidRequest("ID不能为空");
        if (res.hasErrors()) {
            String msg = res.getFieldError().getDefaultMessage();
            return R.unproecsableEntity(msg);
        }
        log.debug("准备执行修改:{}", QuestionVo);
        int o = questionService.updateQuestionById(id,QuestionVo);
        if (o==1){
            return R.ok("修改成功");
        }else {
            return R.invalidRequest("修改失败");
        }

    }

    @GetMapping("/selectMy")
    public R<PageInfo<Question>> selectMy(Integer pageNum,String usernameU){
        if (pageNum == null) {
            pageNum = 1;
        }
        Integer pageSize = 8;
        PageInfo<IndexQuestionVo> list = questionService.getMy(pageNum,pageSize,usernameU);

        return R.ok(list);
    }

    @GetMapping("/news")
    public R selectNews(Integer pageNum){
        if (pageNum == null){
            pageNum = 1;
        }
        Integer pageSize = 10;
        List<NewsVo > list = questionService.selectNews(pageNum,pageSize);
        return R.ok(list);
    }



}
