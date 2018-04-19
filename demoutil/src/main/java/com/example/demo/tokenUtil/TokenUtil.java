package com.example.demo.tokenUtil;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.impl.PublicClaims;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @author
 */

public class TokenUtil {

    private static final String KEY = "demo";

    private static final String ISS = "demo_issuer";

    public static final String TOKEN = "token";
    public static final String REFRESH_TOKEN = "refreshToken";

    public final static String DATE_FULL_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final int INIT_DAY = 0; //初始天数

    /**
     * 登陆有效时间
     *
     * @param state 获取时间状态
     * @param value 时间
     * @return
     */

    public static long getTime(String state, String value) {
        Long time;
        Date date = new Date();
        if (null == state || state.isEmpty()) {
            time = formatLongDate(date, INIT_DAY, 30);
        } else if (TOKEN.equals(state)) {
            if ((null == value || value.isEmpty())) {
                time = formatLongDate(date, INIT_DAY, 60);
            } else {
                time = formatLongDate(date, INIT_DAY, (Integer.parseInt(value) / 2));
            }
        } else if (REFRESH_TOKEN.equals(state)) {
            if ((null == value || value.isEmpty())) {
                time = formatLongDate(date, INIT_DAY, 30);
            } else {
                time = formatLongDate(date, INIT_DAY, (Integer.parseInt(value)));
            }
        } else {
            time = formatLongDate(date, INIT_DAY, 30);
        }
        System.out.println("获取令牌类型state：" + state + "获得value：" + value + "当前时间：" + getDatetimeStr(new Date()) + "获得时间：" + longConvertToDateFormat(time, DATE_FULL_FORMAT));
        return time;
    }


    /**
     * 根据token 字符串转换成对象信息
     *
     * @param token
     * @return
     * @throws Exception
     */
    public static Token getToken(String token) throws Exception {
        if (token == null || token.trim().length() == 0) {
            return null;
        }
        DecodedJWT decodedJWT = getParseObject(token);
        if (null == decodedJWT) {
            return null;
        }
        String userName = decodedJWT.getId();
        Long userId = decodedJWT.getClaim("userId").asLong();
        Token newToken = new Token();
        newToken.setToken(token);
        newToken.setUserId(userId);
        newToken.setUserName(userName);
        return newToken;
    }


    /**
     * 根据redreshToken获取token,
     *
     * @param refreshToken
     * @param time         过期时间
     * @param timeRefresh  refresh过期的时间refreshtoken的时间需要大于session的时间
     * @return Token :刷新token的同时也需要刷新refreshToken
     * @throws Exception
     */
    public static Token getTokenByRefreshToken(String refreshToken, Long time, Long timeRefresh) {
        try {
            DecodedJWT decodedJWT = getParseObject(refreshToken);
            if (decodedJWT == null) {
                return null;
            }
            String userName = decodedJWT.getId();
            Long userId = decodedJWT.getClaim("userId").asLong();

            //定义私有的claim 生成token
            String token = getTokenString(userName, userId, time);
            //定义私有的claim 生成refreshToken
            refreshToken = getTokenString(userName, userId, timeRefresh);

            Token newToken = new Token();
            newToken.setToken(token);
            newToken.setRefreshToken(refreshToken);
            newToken.setUserName(userName);
            newToken.setUserId(userId);
            return newToken;
        } catch (JWTCreationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * http://www.bubuko.com/infodetail-2124829.html
     * iss: token的发行者
     * sub: token的题目
     * aud: token的客户
     * exp: 经常使用的，以数字时间定义失效期，也就是当前时间以后的某个时间本token失效。
     * nbf: 定义在此时间之前，JWT不会接受处理。开始生效时间
     * iat: JWT发布时间，能用于决定JWT年龄
     * jti: JWT唯一标识. 能用于防止 JWT重复使用，一次只用一个token；如果签发的时候这个claim的值是“1”，验证的时候如果这个claim的值不是“1”就属于验证失败
     *
     * @param userName
     * @param userId
     * @param expTime  根据时间确定返回的token类型 ： token/refreshToken
     * @return
     */
    private static String getTokenString(String userName, Long userId, Long expTime) {
        if (null == userName || null == userId || null == expTime) {
            return null;
        }
        try {
            Date expDate = new Date(expTime);
            Algorithm algorithm = Algorithm.HMAC256(KEY);
            Map<String, Object> headerMap = new HashMap<>();
            //type：代表token的类型，这里使用的事JWT类型。alg：使用的Hash算法，例如HMAC SHA256或者RSA。
            headerMap.put(PublicClaims.TYPE, "JWT");
            headerMap.put(PublicClaims.ALGORITHM, algorithm.getName());
            String token = JWT.create().withHeader(headerMap)
                    .withJWTId(userName)
                    .withIssuer(ISS)
                    .withClaim("userId", userId)
                    .withExpiresAt(expDate)
                    .withIssuedAt(new Date())
                    .sign(algorithm);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * token 信息校验
     *
     * @param token
     * @return
     * @throws Exception
     */
    private static DecodedJWT getParseObject(String token) throws Exception {
        try {
            Algorithm algorithm = Algorithm.HMAC256(KEY);
            //要求的算法+发行人
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISS).build();
            return verifier.verify(token);
        } catch (Exception e) {
            System.out.println("token验证出错：" + e);
            return null;
        }
    }


    /**
     * 验证token
     *
     * @param token
     * @return
     * @throws Exception
     */
    public static Boolean validate(String token) throws Exception {
        try {
            DecodedJWT decodedJWT = getParseObject(token);
            if (null == decodedJWT) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }


    public static void main(String[] args) throws Exception {
        String refreshToken = getTokenString("demo", 1l, formatLongDate(new Date(), INIT_DAY, 60));
        validate(refreshToken);
        Token token = getToken(refreshToken);
        System.out.println(token);
        String time = "60";
        token = TokenUtil.getTokenByRefreshToken(refreshToken, TokenUtil.getTime(TOKEN, time), TokenUtil.getTime(REFRESH_TOKEN, time));
        System.out.println(token);
    }


    /**
     * 返回日期long
     *
     * @param date 时间
     * @param day  时间差
     * @return
     */
    public static Long formatLongDate(Date date, int day, int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, min);
        calendar.add(Calendar.DAY_OF_YEAR, day);
        return new Date(calendar.getTimeInMillis()).getTime();
    }

    /**
     * data 类型转换成 String类型
     *
     * @param date
     * @return
     */
    public static String getDatetimeStr(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FULL_FORMAT);
        return sdf.format(date);
    }

    /**
     * long类型的时间 转化成格式化时间
     *
     * @param time
     * @return
     */
    private static String longConvertToDateFormat(Long time, String format) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTimeInMillis(time);
        Date d = cal.getTime();
        String re = new SimpleDateFormat(format).format(d);
        return re;
    }
}
