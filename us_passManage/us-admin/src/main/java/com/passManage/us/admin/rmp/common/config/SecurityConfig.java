package com.passManage.us.admin.rmp.common.config;

import com.passManage.us.admin.rmp.common.handler.MyAccessDeniedHandler;
import com.passManage.us.admin.rmp.common.handler.MyLoginFailHandler;
import com.passManage.us.admin.rmp.common.handler.MyLoginSuccessHandler;
import com.passManage.us.admin.rmp.common.point.MyLoginExpiredAuthenticationEntryPoint;
import com.passManage.us.admin.rmp.common.provider.MyAuthenticationProvider;
import com.passManage.us.admin.rmp.constant.PermissionConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.annotation.Resource;

/**
 * author: www.magicalcoder.com
 * date:2018/7/10
 * function:
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Resource
    private MyAuthenticationProvider myAuthenticationProvider;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
//                .antMatchers("/assets/**","/").permitAll()
                .antMatchers(PermissionConstant.ADMIN_PREFIX+"**").authenticated()
                .and()
                .formLogin()
                //这里就是登录页面配置的action 交给spring处理
//                .loginPage("/login.html")
                .loginProcessingUrl("/login")//登录成功 跳转admin/rmp/index在login.js中处理
                .successHandler(new MyLoginSuccessHandler())
                .failureHandler(new MyLoginFailHandler())
                .and()
                .logout().logoutUrl("/logout").deleteCookies().clearAuthentication(true).invalidateHttpSession(true).logoutSuccessUrl("/").permitAll()
                .and()
                .exceptionHandling().accessDeniedHandler(new MyAccessDeniedHandler()).authenticationEntryPoint(new MyLoginExpiredAuthenticationEntryPoint())
                .and().csrf().disable()
                //设置frame 同源可以嵌入
                .headers().frameOptions().sameOrigin();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(PermissionConstant.STATIC_PREFIX+"**");//忽略静态资源
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(myAuthenticationProvider);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
