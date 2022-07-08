package com.hetongxue.lang;

/**
 * @Description: 全局常量
 * @ClassNmae: Const
 * @Author: 何同学
 * @DateTime: 2022-07-04 16:03
 */
public class Const {
    /**
     * SpringSecurity配置参数
     */
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String VERIFICATION_CODE = "code";
    public static final String REMEMBER_ME = "rememberMe";
    public static final String LOGIN_PATH = "/login";
    public static final String LOGOUT_PATH = "/logout";


    /**
     * 验证码关键字
     **/
    public static final String CAPTCHA_KEY = "captcha";

    /**
     * 权限关键字
     **/
    public static final String AUTHORIZATION_KEY = "Authorization";

    /**
     * JWT密钥
     */
    public static final String SECRET = "568548eddf5fe99ews458dftgv4v87gh";

    /**
     * JWT过期时间设置(默认七天)
     */
    public static final int EXPIRE = 7;

}