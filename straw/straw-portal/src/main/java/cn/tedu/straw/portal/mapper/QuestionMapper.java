package cn.tedu.straw.portal.mapper;

import cn.tedu.straw.commons.model.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author tedu.cn
 * @since 2021-01-13
 */
@Repository
public interface QuestionMapper extends BaseMapper<Question> {

    @Update("update main set views=views+1 where id=#{questionId} ")
    Integer updateViews(Integer questionId);

    @Update("update main set collect=collect+1 where id=#{questionId} ")
    Integer updateCollect(Integer questionId);

    @Select("select count(*) from main where username=#{username}")
    Integer selectQuestionCountByUsername(String username);

    @Update("update main set title=#{title},content=#{content},img=#{img} where id=#{id}")
    Integer updateByQuestionId(String title,String content,String img,Integer id);

}
