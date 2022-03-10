package cn.tedu.straw.resource.controller;


import cn.tedu.straw.commons.service.ServiceException;
import cn.tedu.straw.commons.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//下面的注解表示当前这个类不是一个控制器,而是对控制器功能进行统一增强的增强类
@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {
    //下面这个注解表示下面的方法时专门处理控制器中发生的异常的
    @ExceptionHandler
    public R handlerServiceException(ServiceException e){
        //这个方法会在控制器中发生ServiceException类型异常时自动运行
        log.error("业务异常",e);
        return R.failed(e);
    }
    @ExceptionHandler
    public R handlerException(Exception e){
        log.error("其他异常",e);
        return R.failed(e);
    }


}




