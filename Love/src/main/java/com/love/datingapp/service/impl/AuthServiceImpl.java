package com.love.datingapp.service.impl;

// ... 其他 import ...
import com.love.datingapp.common.dto.LoginDTO;
import com.love.datingapp.common.dto.LoginVO;
import com.love.datingapp.common.dto.RegisterDTO;
import com.love.datingapp.common.utils.JwtUtils;
import com.love.datingapp.entity.User; // 确保 User 实体类已导入
import com.love.datingapp.mapper.UserMapper;
import com.love.datingapp.mapper.UserProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    // ... 其他 @Autowired 属性 ...
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserProfileMapper userProfileMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;

    // ... register 方法不变 ...
    @Override
    @Transactional
    public void register(RegisterDTO registerDTO) {
        // ... register 方法的完整代码 ...
    }

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        User user = userMapper.findByPhone(loginDTO.getPhone());
        if (user == null) {
            throw new RuntimeException("用户不存在或密码错误");
        }

        boolean passwordMatches = passwordEncoder.matches(loginDTO.getPassword(), user.getPassword());
        if (!passwordMatches) {
            throw new RuntimeException("用户不存在或密码错误");
        }

        if (user.getStatus() == 0) {
            throw new RuntimeException("账户已被禁用");
        }

        // 【关键修改】调用我们新的 generateToken 方法，传入角色信息
        String token = jwtUtils.generateToken(user.getId(), user.getRole());

        // 【关键修改】返回包含角色信息的 LoginVO
        return new LoginVO(user.getId(), user.getPhone(), token, user.getRole());
    }
}