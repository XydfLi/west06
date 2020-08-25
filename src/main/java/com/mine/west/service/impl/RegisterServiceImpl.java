package com.mine.west.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.mine.west.constant.AccountConstants;
import com.mine.west.dao.AccountMapper;
import com.mine.west.models.Account;
import com.mine.west.service.AccountService;
import com.mine.west.util.SaltUtils;
import com.mine.west.util.ServletUtils;
import com.mine.west.util.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterServiceImpl {

    @Autowired
    private AccountMapper _accountMapper;   //用户校验

//	@Autowired
//    private PasswordService passwordService;  //密码校验(注册不需要)

    public String register(String username, String password,
                           String mail, String verifyInput) {

        Map<String, Object> map = new HashMap();

        // 验证码校验
        String verifyCode = (String) ServletUtils.getRequest().getSession().getAttribute("verifyCode");
        System.out.println("后端验证码verifyCode:  "+verifyCode);
        if (!StringUtils.isEmpty(verifyCode)){
            if(!verifyInput.equals(verifyCode)) {
                return "验证码不正确";
            }
        }

        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            return "用户名或密码为空";
        }

        // 密码如果不在指定范围内 错误
        if (password.length() < AccountConstants.PASSWORD_MIN_LENGTH
                || password.length() > AccountConstants.PASSWORD_MAX_LENGTH){
            return "密码不在指定范围内";
        }

        // 用户名不在指定范围内 错误
        if (username.length() < AccountConstants.USERNAME_MIN_LENGTH
                || username.length() > AccountConstants.USERNAME_MAX_LENGTH){
            return "用户名不在指定范围内";
        }

        // 查询用户信息(也可以直接查询结果条数--更好)
        Account account1 = _accountMapper.selectAccountByName(username);
        Account account2 = _accountMapper.selectAccountByMailbox(mail);
        if (account1 != null) {  //若用户名已经存在;
            return "用户名已经存在";
        }
        if (account2 != null) {  //若邮箱已经存在
            return "邮箱已经存在";
        }

        //验证邮箱是否符合格式
        if(!maybeEmail(mail)) {
            return "邮箱不符合格式";
        }

        //验证密码是否符合格式
        if(!rightPassword(password)) {
            return "密码不符合格式";
        }


        Account account = new Account();
        //1.生成随机盐：
        String salt = SaltUtils.getSalt(10);
        //2.明文密码进行md5 + salt + hash散列
        Md5Hash md5Hash = new Md5Hash(password, salt,1023);
        //3设置盐值:
        account.setSalt(salt);
        account.setName(username);
        account.setPassword(md5Hash.toHex());
        account.setMailbox(mail);

        if(_accountMapper.insertAccount(account) == 0) {  //插入失败
            return "注册失败....有一股神秘力量.....";
        }
        return "注册成功";
    }

    private boolean maybeEmail(String mail)
    {
        if (!mail.matches(AccountConstants.EMAIL_PATTERN))
        {
            return false;
        }
        return true;
    }

    private boolean rightPassword(String password)
    {
        if (!password.matches(AccountConstants.PASSWORD_PATTERN))
        {
            return false;
        }
        return true;
    }
}
