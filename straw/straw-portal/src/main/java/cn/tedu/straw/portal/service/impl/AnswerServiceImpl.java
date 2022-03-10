package cn.tedu.straw.portal.service.impl;


import cn.tedu.straw.commons.model.Answer;
import cn.tedu.straw.commons.model.News;
import cn.tedu.straw.commons.model.Question;
import cn.tedu.straw.commons.model.User;
import cn.tedu.straw.commons.service.ServiceException;
import cn.tedu.straw.portal.mapper.AnswerMapper;
import cn.tedu.straw.portal.mapper.NewsMapper;
import cn.tedu.straw.portal.mapper.QuestionMapper;
import cn.tedu.straw.portal.mapper.UserMapper;
import cn.tedu.straw.portal.service.IAnswerService;
import cn.tedu.straw.portal.vo.AnswerVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author tedu.cn
 * @since 2021-01-13
 */
@Service
@Slf4j
public class AnswerServiceImpl  implements IAnswerService {

    @Autowired
    AnswerMapper answerMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    NewsMapper newsMapper;

    @Override
    @Transactional
    public void saveAnswer(AnswerVo answerVo, String username) {
        //先查询登录的用户信息
        User user = userMapper.findUserByUsername(username);
        String str = answerVo.getContent();
        log.debug("content:{}", str);
        List list = getImgStr(str);
        StringBuffer url = new StringBuffer();
        for (Object a : list) {
            url.append(a.toString());
        }
        String content = str.replaceAll(url.toString(), "");
        content = content.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "").replaceAll("[(/>)<]", "");
        System.out.println(content);
        if (content==null^content.length()<1){
            throw ServiceException.invalidRequest("评论内容至少包含一个字符");
        }
        Answer answer = new Answer()
                .setQuestionId(answerVo.getQuestionId())
                .setContent(content)
                .setUsername(user.getUsername())
                .setTime(LocalDateTime.now())
                .setHead(user.getImg());
        if (url.length() <= 0) {
            answer.setImg(null);
        } else {
            answer.setImg(url.toString());
        }
        //执行新增
        int num = answerMapper.insert(answer);
        if (num != 1) {
            throw ServiceException.busy();
        }

        Question question = questionMapper.selectById(answerVo.getQuestionId());
        if (!question.getUsername().equals(user.getUsername())) {
            News news = new News()
                    .setUsername(question.getUsername())
                    .setUsernameU(user.getUsername())
                    .setQuestionId(question.getId())
                    .setTitle(question.getTitle())
                    .setContent(content);
            int a = newsMapper.insert(news);
            if (a != 1) {
                throw ServiceException.busy();
            }
        }

    }

    private static Pattern p_image = Pattern.compile("<img.*src\\s*=\\s*(.*?)[^>]*?>", Pattern.CASE_INSENSITIVE);
    private static Pattern r_image = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)");


    public static List<String> getImgStr(String htmlStr) {
        List<String> list = new ArrayList<>();
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        Matcher m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            // 得到<img />数据
            String img = m_image.group();
            System.out.println(img);
            // 匹配<img>中的src数据
            Matcher m = r_image.matcher(img);
            while (m.find()) {
                list.add(m.group(1));
            }
        }
        return list;
    }

    @Override
    public List<Answer> getAnswersByQuestionId(String questionId) {
        if (questionId == null)
            throw ServiceException.notFound("id不能为空");
        List<Answer> answers = answerMapper.findAnswersWithCommentByQuestionId(questionId);

        return answers;

    }
}
