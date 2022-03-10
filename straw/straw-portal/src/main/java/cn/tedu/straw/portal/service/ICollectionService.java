package cn.tedu.straw.portal.service;

public interface ICollectionService {
    Boolean addCollection(Integer questionId);

    Boolean selectCollectByQuestionId(Integer questionId);

    Boolean deleteCollectByQuestionId(Integer questionId);
}
