package com.demo.taotao.validate.aop;


import com.alibaba.fastjson.JSONObject;
import com.demo.taotao.validate.annotation.CheckParam;
import com.demo.taotao.validate.annotation.CheckParams;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class CheckParamAop {


    @Pointcut("@annotation(com.demo.taotao.validate.annotation.CheckParam)")
    public void checkParam() {
    }

    @Pointcut("@annotation(com.demo.taotao.validate.annotation.CheckParams)")
    public void checkParams() {
    }

    @Around("checkParam()") // 这里要换成自定义注解的路径
    public Object check1(ProceedingJoinPoint point) throws Throwable {
        Object obj;
        // 参数校验
        String msg = doCheck(point, false);
        if (null != msg) {
            throw new IllegalArgumentException(msg);
        }
        // 通过校验，继续执行原有方法
        obj = point.proceed();
        return obj;
    }

    @Around("checkParams()") // 这里要换成自定义注解的路径
    public Object check2(ProceedingJoinPoint point) throws Throwable {
        Object obj;
        // 参数校验
        String msg = doCheck(point, true);
        if (null != msg) {
            throw new IllegalArgumentException(msg);
        }
        // 通过校验，继续执行原有方法
        obj = point.proceed();
        return obj;
    }

    /**
     * 参数校验
     * @param point 切点
     * @param multi 多参数校验
     * @return 错误信息
     */
    private String doCheck(JoinPoint point, boolean multi) {
        Method method = this.getMethod(point);
        String[] paramName = this.getParamName(point);
        Object[] arguments = point.getArgs();	// 获取接口传递的所有参数

        Boolean isValid = true;
        String msg = null;
        if(multi) {	// 多个参数校验
            CheckParams annotation = method.getAnnotation(CheckParams.class);	// AOP监听带注解的方法，所以不用判断注解是否为空
            CheckParam[] annos = annotation.value();
            for (CheckParam anno : annos) {
                String argName = anno.argName();
                Object value = this.getParamValue(arguments, paramName, argName);	//参数值
                isValid = anno.value().fun.apply(value, anno.express());	// 执行判断 // 调用枚举类的 CheckUtil类方法
                if(!isValid) {	// 只要有一个参数判断不通过，立即返回
                    msg = anno.msg();
                    if(null == msg || "".equals(msg)) {
                        msg = argName + ": " + anno.value().msg + " " + anno.express();
                    }
                    break;
                }
            }
        } else {	// 单个参数校验
            CheckParam anno = method.getAnnotation(CheckParam.class);		// AOP监听带注解的方法，所以不用判断注解是否为空

            String argName = anno.argName();
            Object value = this.getParamValue(arguments, paramName, argName);	//参数值
            isValid = anno.value().fun.apply(value, anno.express());	// 执行判断 // 调用枚举类的 CheckUtil类方法
            msg = anno.msg();
            if(null == msg || "".equals(msg)) {
                msg = argName + ": " + anno.value().msg + " " + anno.express();
            }
        }
        if(isValid) {
            // log.info("参数校验通过");
            return null;
        } else {
            // log.error("参数校验不通过");
            return msg ;
        }
    }

    private String[] getParamName(JoinPoint point) {
        return null;
    }

    private Method getMethod(JoinPoint point) {
        return null;
    }

    /**
     * 根据参数名称，获取参数值
     */
    private Object getParamValue(Object[] arguments, String[] paramName, String argName) {
        Object value = null;
        String name = argName;
        if(argName.contains(".")) {
            name = argName.split("\\.")[0];
        }
        int index = 0;
        for (String string : paramName) {
            if(string.equals(name)) {
                value = arguments[index];	//基本类型取值	// 不做空判断，如果注解配置的参数名称不存在，则取值为null
                break;
            }
            index++;
        }
        if(argName.contains(".")) {	//从对象中取值
            argName = argName.split("\\.")[1];
            JSONObject jo = (JSONObject) JSONObject.toJSON(value);
            // 从实体对象中取值
            value = jo.get(argName);
        }
        return value;
    }

}
