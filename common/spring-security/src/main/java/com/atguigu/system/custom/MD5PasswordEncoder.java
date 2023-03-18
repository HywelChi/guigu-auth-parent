package com.atguigu.system.custom;

import com.atguigu.common.util.MD5;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: com.atguigu.system.custom
 * @Author: hywel
 * @CreateTime: 2023-03-15  11:25
 * @Description: TODO
 * @Version: 1.0
 */
@Component
public class MD5PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return MD5.encrypt(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return MD5.encrypt(rawPassword.toString()).equals(encodedPassword);
    }
}
