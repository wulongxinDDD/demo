package cn.tedu.straw.portal.security;

import cn.tedu.straw.portal.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    //配置登录验证(即用户名和密码的验证)
    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()   //关闭防跨域攻击
                .authorizeRequests()//开始设置授权范围
                .antMatchers(
                        "/img/*",
                        "/js/*",
                        "/css/*",
                        "/bower_components/**",
                        "/login.html",
                        "/register.html",
                        "/register"
                ).permitAll()     //上面路径允许直接访问
                .anyRequest().authenticated()//其他的资源需要登录
                .and().formLogin()//登录方式是表单
                .loginPage("/login.html")//指定登录的页面
                .loginProcessingUrl("/login")//当登录页面提交时,会提交给哪个路径
                .failureUrl("/login.html?error")//登录失败从新登录,显示错误提示
                .defaultSuccessUrl("/index.html")//登录成功跳转到index.html
                .and().logout() //配置登出
                .logoutUrl("/logout")//设置登出路径
                //登出成功跳转登录页面,并提示已登出
                .logoutSuccessUrl("/login.html?logout");
    }
}
