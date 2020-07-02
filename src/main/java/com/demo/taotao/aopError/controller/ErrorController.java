package com.demo.taotao.aopError.controller;


import com.demo.taotao.aopError.util.EmailUtil;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import net.sf.json.JSONObject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * <dependency>
 *       <groupId>org.aspectj</groupId>
 *       <artifactId>aspectjweaver</artifactId>
 *       <version>1.8.13</version>
 * </dependency>
 *
 *<dependency>
 *     <groupId>org.apache.lucene</groupId>
 *     <artifactId>lucene-core</artifactId>
 *     <version>8.3.0</version>
 * </dependency>
 */

@Component
@Aspect
public class ErrorController {

    private Logger log = LoggerFactory.getLogger(ErrorController.class);

    private Config config;

    @Autowired
    private EmailUtil emailUtil;


    @Around("execution(* com.demo.taotao.aopError.controller..*.*(..))")
    public Object around(ProceedingJoinPoint pj) throws Throwable {
        long starttime=System.currentTimeMillis();

        try {
            log.info("---------------------API请求参数：【" + getMessage(pj, null) + "】");
            Object obj = pj.proceed();
            long size = RamUsageEstimator.sizeOf(obj);
            if (size < 2 * 1024) {
                //2kb
                log.info("---------------------API返回值：【" + "类名:{},方法名：{};返回值:{}】", pj.getTarget().getClass().getSimpleName(),
                        pj.getSignature().getName(), JSONObject.fromObject(obj));
            }
        }catch (MethodArgumentNotValidException e) {
            e.getBindingResult().getGlobalErrors();
            return "请求参数验证失败";
        } catch (SocketTimeoutException e) {
            concatError(starttime, getMessage(pj, e));
            return "EXCEPTION_TIMEOUT_ERROR";
        } catch (Exception e) {
            concatError(starttime, getMessage(pj, e));
            return "EXCEPTION_BUSY";
        }

        return null;
    }


    private String getMessage(ProceedingJoinPoint pj, Exception e){

        if (e != null) {
            log.error(e.getMessage(), e);
        }
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        String token = request.getHeader("AUTHORIZATION");
        String userInfo = request.getHeader("HEAD_USER_INFO");
        StringBuilder joiner = new StringBuilder("<h3>链接:</h3>")
                .append(request.getRequestURI())
                .append("<br/><h3>token:</h3>")
                .append((token == null ? "" : token))
                .append("<br/><h3>user phone info:</h3>")
                .append((userInfo == null ? "" : userInfo))
                .append(";<br/><h3>类名:</h3>")
                .append(pj.getTarget().getClass().getSimpleName())
                .append(";<br/><h3>方法:</h3>")
                .append(pj.getSignature().getName())
                .append(";<br/><h3>参数:</h3>");
        Object[] args = pj.getArgs();
        List<Object> objects=new ArrayList<>();
        for(Object object:args){
            if(object instanceof MultipartFile || object instanceof File){
                continue;
            }
            objects.add(object);
        }
        joiner.append(JSONObject.fromObject(objects));
        if (Objects.nonNull(e)) {
            joiner .append(";<br/><h3>message:</h3>")
                    .append(e.getMessage())
                    .append(";<br/><h3>异常:</h3>")
                    .append((e));
        }
        return joiner.toString();
    }


    private void concatError(long starttime, String message) {
        StringBuilder stringBuilder=new StringBuilder( "<h3>接口耗时:</h3>");
        stringBuilder.append("接口花费时间：").append(System.currentTimeMillis()-starttime).append("ms<br/>");
        stringBuilder.append(message);
        sendErrorNotice(stringBuilder.toString());
    }


    private void sendErrorNotice(String content) {
        log.error(content);
        boolean enable = config.getBooleanProperty("exception.sendemail.enable", false);
        if (enable) {
            emailUtil.send(content);//发送邮件，具体邮件配置可以参考我的资源中的邮件发送 源码
        }
    }



}
