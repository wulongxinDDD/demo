package cn.tedu.straw.portal.service;


import cn.tedu.straw.commons.model.Answer;
import cn.tedu.straw.portal.vo.AnswerVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author tedu.cn
 * @since 2021-01-13
 */
public interface IAnswerService  {

    // 评论方法
    void saveAnswer(AnswerVo answerVo, String username);

    //获得指定问题id的所有回答
    List<Answer> getAnswersByQuestionId(String questionId);

}
