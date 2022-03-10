package cn.tedu.straw.portal.service.impl;

import cn.tedu.straw.commons.model.Collect;
import cn.tedu.straw.commons.model.News;
import cn.tedu.straw.commons.model.Question;
import cn.tedu.straw.commons.model.User;
import cn.tedu.straw.commons.service.ServiceException;
import cn.tedu.straw.portal.mapper.CollectionMapper;
import cn.tedu.straw.portal.mapper.NewsMapper;
import cn.tedu.straw.portal.mapper.QuestionMapper;
import cn.tedu.straw.portal.mapper.UserMapper;
import cn.tedu.straw.portal.service.ICollectionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CollectionServiceImpl implements ICollectionService {
    @Resource
    CollectionMapper collectionMapper;
    @Resource
    QuestionMapper questionMapper;
    @Autowired
    NewsMapper newsMapper;
    @Autowired
    UserMapper userMapper;
    @Override
    public Boolean addCollection(Integer questionId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw ServiceException.invalidRequest("没有登录!");
        }
        String username = authentication.getName();
        User user = userMapper.findUserByUsername(username);
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",user.getUsername());
        queryWrapper.eq("question_id",questionId);
        Integer count = collectionMapper.selectCount(queryWrapper);
        if (count==0) {
            Collect collect = new Collect().setUsername(user.getUsername()).setQuestionId(questionId);
            int a = collectionMapper.insert(collect);
            if (a == 1) {
                questionMapper.updateCollect(questionId);
                Question question = questionMapper.selectById(questionId);
                News news = new News()
                        .setUsername(question.getUsername())
                        .setUsernameU(user.getUsername())
                        .setQuestionId(question.getId())
                        .setTitle(question.getTitle());
                newsMapper.insert(news);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public Boolean selectCollectByQuestionId(Integer questionId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw ServiceException.invalidRequest("没有登录!");
        }
        String username = authentication.getName();
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        queryWrapper.eq("question_id",questionId);
        Integer count = collectionMapper.selectCount(queryWrapper);
        if (count==1){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Boolean deleteCollectByQuestionId(Integer questionId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw ServiceException.invalidRequest("没有登录!");
        }
        String username = authentication.getName();
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        queryWrapper.eq("question_id",questionId);
        System.out.println("--------------");
        System.out.println(username);
        System.out.println(questionId);
        int a = collectionMapper.delete(queryWrapper);
        if (a==1){
            return true;
        }else {
            return false;
        }
    }
}
