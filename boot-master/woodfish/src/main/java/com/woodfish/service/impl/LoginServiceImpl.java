package com.woodfish.service.impl;

import com.woodfish.entity.User;
import com.woodfish.service.ILoginService;
import com.woodfish.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sheng
 * @since 2023-01-14
 */
@Service
public class LoginServiceImpl  implements ILoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Map<String, String> getToken(String username, String password) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Map<String, String> map = new HashMap<>();

        // 如果登陆失败，会自动处理
        try {
            Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();
            User user = loginUser.getUser();
            String Jwt = JwtUtil.createJWT(user.getId().toString());

            map.put("error_message", "success");
            map.put("token", Jwt);
            return map;
        } catch (Exception e) {
            map.put("error_message", "登录失败");
        }

        return map;
    }
}
