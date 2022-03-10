package cn.tedu.straw.sys;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class StrawSysApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    PasswordEncoder passwordEncoder;

    //加密
    @Test
    public void encode() {
        String pwd = passwordEncoder.encode("123456");
        System.out.println(pwd);
        //$2a$10$ytxmGeWcRZObqoDmlhnWxe6KqUjb9DTONmQVKkmwneHQtZw4LQtiq
    }

}
