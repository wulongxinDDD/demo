package cn.tedu.straw.portal.service.impl;

import cn.tedu.straw.commons.model.*;
import cn.tedu.straw.commons.service.ServiceException;
import cn.tedu.straw.portal.mapper.*;
import cn.tedu.straw.portal.service.IQuestionService;
import cn.tedu.straw.portal.service.IUserService;
import cn.tedu.straw.portal.vo.IndexQuestionVo;
import cn.tedu.straw.portal.vo.NewsVo;
import cn.tedu.straw.portal.vo.QuestionDetailVo;
import cn.tedu.straw.portal.vo.QuestionVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
public class QuestionServiceImpl implements IQuestionService {

    @Autowired
    IUserService userService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    BlackListMapper blackListMapper;
    @Autowired
    CollectionMapper collectionMapper;
    @Autowired
    FriendMapper friendMapper;

    @Override
    public PageInfo<IndexQuestionVo> getMyQuestions(Integer pageNum, Integer pageSize) {
        if (pageNum == null || pageSize == null) {
            throw ServiceException.invalidRequest("缺失分页参数!");
        }
        //获得当前登录用户的用户名
        String username = userService.currentUsername();
        //根据用户名获得用户对象(包括id的)
        User u = userMapper.findUserByUsername(username);
        log.debug("获得的用户为:{}", u);
        if (u == null) {
            throw ServiceException.gone("登录用户不存在!");
        }
        QueryWrapper<Collect> collectQueryWrapper = new QueryWrapper<>();
        collectQueryWrapper.eq("username",username);
        List<Collect> collectList = collectionMapper.selectList(collectQueryWrapper);

        QueryWrapper<Friend> friendQueryWrapper = new QueryWrapper<>();
        friendQueryWrapper.eq("fans",username);
        List<Friend> friendList = friendMapper.selectList(friendQueryWrapper);

        QueryWrapper<BlackList> queryA = new QueryWrapper<>();
        queryA.eq("b_username", username);
        List<BlackList> listA = blackListMapper.selectList(queryA);
        QueryWrapper<BlackList> queryB = new QueryWrapper<>();
        queryB.eq("username", username);
        List<BlackList> listB = blackListMapper.selectList(queryB);

        QueryWrapper<Question> query = new QueryWrapper<>();
        for (int i = 0; i < listB.size(); i++) {
            query.ne("username", listB.get(i).getBUsername());
        }
        for (int i = 0;i<listA.size();i++){
            query.ne("username", listA.get(i).getUsername());
        }
        query.orderByDesc("time");

        int total = questionMapper.selectCount(query);
        PageHelper.startPage(pageNum,pageSize);
        List<Question> questionList = questionMapper.selectList(query);

        List<IndexQuestionVo> indexQuestionVoList = new ArrayList<>();
        for (Question question:questionList){
            IndexQuestionVo indexQuestionVo = new IndexQuestionVo()
                    .setId(question.getId())
                    .setCollect(question.getCollect())
                    .setContent(question.getContent())
                    .setSendname(userMapper.findUserByUsername(question.getUsername()).getNickname())
                    .setUsername(question.getUsername())
                    .setImg(question.getImg())
                    .setTitle(question.getTitle())
                    .setTime(question.getTime())
                    .setViews(question.getViews());
            A: for (Collect collect:collectList){
                if (collect.getQuestionId()==question.getId()) {
                    indexQuestionVo.setCollection(true);
                    break A;
                }
            }
            B: for (Friend friend:friendList){
                if (friend.getFollow().equals(question.getUsername())){
                    indexQuestionVo.setFriend(true);
                    break B;
                }
            }
            indexQuestionVoList.add(indexQuestionVo);
        }
        log.debug("查询出{}个主题", indexQuestionVoList.size());
        log.debug("集合{}", indexQuestionVoList);

        PageInfo<IndexQuestionVo> page = getPageInfo(pageNum,pageSize,indexQuestionVoList,total);
        return page;
    }

    public static <T> PageInfo<T> getPageInfo(int currentPage, int pageSize, List<T> list , int total) {
        int pages=0;
        if (total%pageSize==0){
            pages=total/pageSize;
        }else {
            pages=(total/pageSize)+1;
        }
        Page<T> page = new Page<>(currentPage, pageSize);
        page.addAll(list);
        page.setPages(pages);
        page.setTotal(total);

        return new PageInfo<>(page);
    }


    @Override
    //@Transactional注解
    //这个注解会将下面方法中的所有数据库操作定义为一个事务
    //如果运行过程中发生任何异常而结束方法,数据库中的数据会自动回滚到
    //这个方法运行之前的状态
    @Transactional
    public void saveQuestion(QuestionVo questionVo) {
        //获得登录用户信息
        String username = userService.currentUsername();
        User user = userMapper.findUserByUsername(username);
        log.debug("获得登录用户信息:{}", user);
        String str = questionVo.getContent();
        log.debug("content:{}", str);
        List list = getImgStr(str);
        StringBuffer url = new StringBuffer();
        for (Object a : list) {
            url.append(a.toString());
        }
        String content = str.replaceAll(url.toString(), "");
        content = content.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "").replaceAll("[(/>)<]", "");
        if (content==null^content.length()<1){
            throw ServiceException.invalidRequest("内容至少包含一个字符");
        }
        if (url.length() <= 0) {
            url.append("/img/tags/iot.jpg");
        }
        Question question = new Question()
                .setContent(content)
                .setTitle(questionVo.getTitle())
                .setTime(LocalDateTime.now())
                .setUsername(user.getUsername())
                .setImg(url.toString())
                .setViews(0)
                .setCollect(0);
        log.debug("img{}", url.toString());
        //执行Question的新增
        int num = questionMapper.insert(question);
        if (num != 1) {
            throw ServiceException.busy();
        }
        log.debug("新增问题完成:{}", question);

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
    public QuestionDetailVo getQuestionById(Integer id) {
        Question question = questionMapper.selectById(id);
        User user = userMapper.findUserByUsername(question.getUsername());
        QuestionDetailVo questionDetailVo = new QuestionDetailVo()
                .setBirthday(user.getBirthday())
                .setGender(user.getGender())
                .setCollect(question.getCollect())
                .setContent(question.getContent())
                .setSendname(userMapper.findUserByUsername(question.getUsername()).getNickname())
                .setImg(question.getImg())
                .setViews(question.getViews())
                .setId(question.getId())
                .setTitle(question.getTitle())
                .setUsername(question.getUsername())
                .setTime(question.getTime());

        return questionDetailVo;
    }

    @Override
    public int deleteQuestionById(Integer id) {
        return questionMapper.deleteById(id);
    }

    @Override
    public int updateQuestionById(Integer id,QuestionVo QuestionVo) {
        String str = QuestionVo.getContent();
        log.debug("content:{}", str);
        List list = getImgStr(str);
        StringBuffer url = new StringBuffer();
        for (Object a : list) {
            url.append(a.toString());
        }
        String content = str.replaceAll(url.toString(), "");
        if (url.length() <= 0) {
            Question question = questionMapper.selectById(id);
            url.append(question.getImg());
        }
        return questionMapper.updateByQuestionId(QuestionVo.getTitle(),content,url.toString(),id);
    }

    @Override
    public PageInfo<IndexQuestionVo> getMy(Integer pageNum, Integer pageSize,String usernameU) {
        if (pageNum == null || pageSize == null) {
            throw ServiceException.invalidRequest("缺失分页参数!");
        }
        String username = userService.currentUsername();
        if (usernameU == null&&username == null) {
            throw ServiceException.gone("username不能为空!");
        }

        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",usernameU);
        int total = questionMapper.selectCount(queryWrapper);
        PageHelper.startPage(pageNum,pageSize);
        List<Question> questionList = questionMapper.selectList(queryWrapper);

        QueryWrapper<Collect> collectQueryWrapper = new QueryWrapper<>();
        collectQueryWrapper.eq("username",username);
        List<Collect> collectList = collectionMapper.selectList(collectQueryWrapper);

        List<IndexQuestionVo> indexQuestionVoList = new ArrayList<>();
        for (Question question:questionList){
            IndexQuestionVo indexQuestionVo = new IndexQuestionVo()
                    .setId(question.getId())
                    .setCollect(question.getCollect())
                    .setContent(question.getContent())
                    .setSendname(userMapper.findUserByUsername(question.getUsername()).getNickname())
                    .setUsername(question.getUsername())
                    .setImg(question.getImg())
                    .setTitle(question.getTitle())
                    .setTime(question.getTime())
                    .setViews(question.getViews());
            A: for (Collect collect:collectList){
                if (collect.getQuestionId()==question.getId()) {
                    indexQuestionVo.setCollection(true);
                    break A;
                }
            }
            indexQuestionVoList.add(indexQuestionVo);
        }
        PageInfo<IndexQuestionVo> page = getPageInfo(pageNum,pageSize,indexQuestionVoList,total);
        return page;
    }

    @Autowired
    NewsMapper newsMapper;
    @Override
    public List<NewsVo> selectNews(Integer pageNum, Integer pageSize) {
        String username = userService.currentUsername();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",username);
        List<News> newsList = newsMapper.selectList(queryWrapper);
        List<NewsVo> newsVoList = new ArrayList<>();
        for (News  list: newsList){
            NewsVo newsVo = new NewsVo()
                    .setUsername(list.getUsername())
                    .setSendname(userMapper.findUserByUsername(list.getUsername()).getNickname())
                    .setContent(list.getContent())
                    .setQuestionId(list.getQuestionId())
                    .setTitle(list.getTitle())
                    .setUsernameU(list.getUsernameU());
            newsVoList.add(newsVo);
        }
        Collections.reverse(newsVoList);
        return newsVoList;
    }

}
