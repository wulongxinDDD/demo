package cn.tedu.straw.portal.mapper;

import cn.tedu.straw.commons.model.Answer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author tedu.cn
 * @since 2021-01-13
 */
@Repository
public interface AnswerMapper extends BaseMapper<Answer> {
    //根据问题id查询所有回答以及回答的所有评论的方法
    @Select("SELECT * FROM answers where question_id=#{qid}")
    List<Answer> findAnswersWithCommentByQuestionId(String qid);

}
