package cn.tedu.straw.portal.service;


import cn.tedu.straw.portal.vo.IndexQuestionVo;
import cn.tedu.straw.portal.vo.NewsVo;
import cn.tedu.straw.portal.vo.QuestionDetailVo;
import cn.tedu.straw.portal.vo.QuestionVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author tedu.cn
 * @since 2021-01-13
 */
public interface IQuestionService  {


    PageInfo<IndexQuestionVo> getMyQuestions(Integer pageNum, Integer pageSize);

    //学生发布问题的方法
    void saveQuestion(QuestionVo questionVo);

    //根据问题id查询问题方法
    QuestionDetailVo getQuestionById(Integer id);


    int deleteQuestionById(Integer id);


    int updateQuestionById(Integer id,QuestionVo updateQuestionVo);


    PageInfo<IndexQuestionVo> getMy(Integer pageNum, Integer pageSize,String usernameU);

    List<NewsVo> selectNews(Integer pageNum, Integer pageSize);
}
