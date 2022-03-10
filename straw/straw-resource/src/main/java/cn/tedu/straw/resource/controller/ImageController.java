package cn.tedu.straw.resource.controller;

import cn.tedu.straw.commons.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/v1/images")
@Slf4j
public class ImageController {

    @Value("${spring.resources.static-locations}")
    private File resourcePath;

    @Value("${straw.resource.host}")
    private String resourceHost;
    //稻草项目上传富文本编辑器中选中图片的方法
    @PostMapping
    public R<String> uploadImage(MultipartFile imageFile) throws IOException {
        //服务器每天新建一个文件夹保存当天上传的文件,方便管理
        //文件夹按年\月\日来分割
        String path= DateTimeFormatter.ofPattern("yyyy/MM/dd")
                .format(LocalDate.now());
        File folder=new File(resourcePath,path);
        //为了保证文件夹测存在,要创建文件夹
        folder.mkdirs();
        log.debug("上传的文件夹为:{}",folder.getAbsolutePath());
        String filename=imageFile.getOriginalFilename();
        //获取后缀名(扩展名)
        String ext=filename.substring(
                filename.lastIndexOf("."));// .png
        log.debug("文件名:{},后缀名:{}",filename,ext);

        //随机生成文件名
        //使用UUID来获得随机字符串
        String name= UUID.randomUUID().toString()+ext;
        //E:/upload/2021/01/20/uuid.png
        File file=new File(folder,name);

        log.debug("最终文件名:{}",file.getAbsolutePath());
        //写入文件
        imageFile.transferTo(file);
        //返回静态资源服务器访问该图片的路径
        String url=resourceHost+"/"+path+"/"+name;
        log.debug("访问路径为:{}",url);
        return R.ok(url);
    }
}
