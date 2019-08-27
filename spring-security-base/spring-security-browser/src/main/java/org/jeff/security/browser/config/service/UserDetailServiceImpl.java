package org.jeff.security.browser.config.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * <p>Date: 2019-08-23 16:17:00</p>
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        logger.info("登陆用户名：" + username);
        //根据用户名查询用户信息
//        User user = new User(username, "123456", AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        //应该在用户注册时的加密操作，从数据库获取的密码应该是加密后的
        String password = passwordEncoder.encode("123456");
        User user = new User(username, password, true, true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));

        return user;
    }

}
