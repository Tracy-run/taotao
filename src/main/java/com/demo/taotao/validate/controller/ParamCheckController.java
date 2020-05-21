package com.demo.taotao.validate.controller;

import com.demo.taotao.validate.annotation.CheckParam;
import com.demo.taotao.validate.annotation.CheckParams;
import com.demo.taotao.validate.enumtype.Check;
import com.demo.taotao.validate.vo.DeptEntity;
import com.demo.taotao.validate.vo.EmployeeEntity;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paramCheck")
public class ParamCheckController {

    @ApiImplicitParam(name = "userId", value = "", dataType = "Integer", paramType="query")
    @CheckParam(value = Check.NotNull, argName = "userId")
    @PostMapping("/singleCheckNotNull")
    public Object singleCheckNotNull(Integer userId) {
        System.err.println(userId);
        return 1;
    }

    @ApiImplicitParam(name = "userName", value = "", dataType = "String", paramType="query")
    @CheckParam(value = Check.NotEmpty, argName = "userName", msg = "你大爷的，这个是必填参数！")
    @PostMapping("/singleCheckNotEmpty")
    public Object singleCheckNotNull(String userName) {
        System.err.println(userName);
        return 1;
    }

    @ApiImplicitParam(name = "bl", value = "", dataType = "Boolean", paramType="query")
    @CheckParam(value = Check.True, argName = "bl")
    @PostMapping("/singleCheckTrue")
    public Object singleCheckTrue(Boolean bl) {
        System.err.println(bl);
        return 1;
    }

    @ApiImplicitParam(name = "date", value = "", dataType = "String", paramType="query")
    @CheckParam(value = Check.Date, argName = "date")
    @PostMapping("/singleCheckDate")
    public Object singleCheckDate(String date) {
        System.err.println(date);
        return 1;
    }

    @ApiImplicitParam(name = "dateTime", value = "", dataType = "String", paramType="query")
    @CheckParam(value = Check.DateTime, argName = "dateTime")
    @PostMapping("/singleCheckDateTime")
    public Object singleCheckDateTime(String dateTime) {
        System.err.println(dateTime);
        return 1;
    }

    @ApiImplicitParam(name = "date", value = "", dataType = "String", paramType="query")
    @CheckParam(value = Check.Past, argName = "date")
    @PostMapping("/singleCheckPast")
    public Object singleCheckPast(String date) {
        System.err.println(date);
        return 1;
    }

    @ApiImplicitParam(name = "dateTime", value = "", dataType = "String", paramType="query")
    @CheckParam(value = Check.Future, argName = "dateTime", msg = "参数必须是一个将来的日期或者时间，并且满足 yyyy-MM-dd HH:mm:ss格式")
    @PostMapping("/singleCheckFuture")
    public Object singleCheckFuture(String dateTime) {
        System.err.println(dateTime);
        return 1;
    }

    @ApiImplicitParam(name = "date", value = "", dataType = "String", paramType="query")
    @CheckParam(value = Check.Today, argName = "date")
    @PostMapping("/singleCheckToday")
    public Object singleCheckToday(String date) {
        System.err.println(date);
        return 1;
    }

    @ApiImplicitParam(name = "gender", value = "", dataType = "String", paramType="query")
    @CheckParam(value = Check.Enum, argName = "gender", express="男,女,太监")
    @PostMapping("/singleCheckStringEnum")
    public Object singleCheckStringEnum(String gender) {
        System.err.println(gender);
        return 1;
    }

    @ApiImplicitParam(name = "gender", value = "", dataType = "Integer", paramType="query")
    @CheckParam(value = Check.Enum, argName = "gender", express="0,1")
    @PostMapping("/singleCheckIntegerEnum")
    public Object singleCheckIntegerEnum(Integer gender) {
        System.err.println(gender);
        return 1;
    }

    @ApiImplicitParam(name = "password", value = "", dataType = "String", paramType="query")
    @CheckParam(value = Check.Length, argName = "password", express="6,18", msg="密码length必须在6-18位之间！")
    @PostMapping("/singleCheckStringLength")
    public Object singleCheckStringLength(String password) {
        System.err.println(password);
        return 1;
    }

    @ApiImplicitParam(name = "password", value = "", dataType = "String", paramType="query")
    @CheckParams({
            @CheckParam(value = Check.ge, argName = "password", express = "6"),
            @CheckParam(value = Check.le, argName = "password", express = "18")
    })
    @PostMapping("/singleCheckStringLength1")
    public Object singleCheckStringLength1(String password) {
        System.err.println(password);
        return 1;
    }

    @ApiImplicitParam(name = "age", value = "", dataType = "Integer", paramType="query")
    @CheckParam(value = Check.Range, argName = "age", express="18,50")
    @PostMapping("/singleCheckIntegerRange")
    public Object singleCheckIntegerRange(Integer age) {
        System.err.println(age);
        return 1;
    }

    @ApiImplicitParam(name = "age", value = "", dataType = "Integer", paramType="query")
    @CheckParams({
            @CheckParam(value = Check.ge, argName = "age", express="18"),
            @CheckParam(value = Check.le, argName = "age", express="50")
    })
    @PostMapping("/singleCheckIntegerRange1")
    public Object singleCheckIntegerRange1(Integer age) {
        System.err.println(age);
        return 1;
    }

    @ApiImplicitParam(name = "age", value = "", dataType = "Integer", paramType="query")
    @CheckParam(value = Check.NotIn, argName = "age", express="18,50")
    @PostMapping("/singleCheckIntegerNotIn")
    public Object singleCheckIntegerNotIn(Integer age) {
        System.err.println(age);
        return 1;
    }

    @ApiImplicitParam(name = "age", value = "", dataType = "Integer", paramType="query")
    @CheckParams({
            @CheckParam(value = Check.lt, argName = "age", express="18"),
            @CheckParam(value = Check.gt, argName = "age", express="50")
    })
    @PostMapping("/singleCheckIntegerNotIn1")
    public Object singleCheckIntegerNotIn1(Integer age) {
        System.err.println(age);
        return 1;
    }

    @ApiImplicitParam(name = "email", value = "", dataType = "String", paramType="query")
    @CheckParam(value = Check.Email, argName = "email", msg="你大爷的，输入个邮箱啊！")
    @PostMapping("/singleCheckEmail")
    public Object singleCheckEmail(String email) {
        System.err.println(email);
        return 1;
    }

    @ApiImplicitParam(name = "age", value = "", dataType = "Integer", paramType="query")
    @CheckParam(value = Check.ge, argName = "age", express="18", msg = "必须大于等于18岁") // gt、lt、le、ne、Equal不再举例; 具体看注释
    @PostMapping("/singleCheckIntegerGe")
    public Object singleCheckIntegerGe(Integer age) {
        System.err.println(age);
        return 1;
    }

    @ApiImplicitParam(name = "pattern", value = "", dataType = "String", paramType="query")
    @CheckParam(value = Check.Pattern, argName = "pattern", express="^[\u0021-\u007E]{4,16}$")
    @PostMapping("/singleCheckPattern")
    public Object singleCheckPattern(String pattern) {
        System.err.println(pattern);
        return 1;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", dataType = "Integer", paramType="query"),
            @ApiImplicitParam(name = "userName", dataType = "String", paramType="query")
    })
    @CheckParams({
            @CheckParam(value = Check.NotNull, argName = "userId", msg = "你大爷的，这个是必填参数！"),
            @CheckParam(value = Check.NotNull, argName = "userName")
    })
    @PostMapping("/multiCheckNotNull")
    public Object multiCheckNotNull(Integer userId, String userName) {
        System.err.println(userId);
        System.err.println(userName);
        return 1;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", dataType = "Integer", paramType="query"),
            @ApiImplicitParam(name = "userName", dataType = "String", paramType="query"),
            @ApiImplicitParam(name = "employee", dataType = "entity", paramType="body")
    })
    @CheckParams({
            @CheckParam(value = Check.NotNull, argName = "userId", msg = "你大爷的，这个是必填参数！"),
            @CheckParam(value = Check.NotEmpty, argName = "userName"),
            @CheckParam(value = Check.NotEmpty, argName = "employee.name")
    })
    @PostMapping("/entityCheckNotNull")
    public Object entityCheckNotNull(Integer userId, String userName, @RequestBody EmployeeEntity employee) {
        System.err.println(userId);
        System.err.println(userName);
//        System.err.println(employee.getName());
        return 1;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", dataType = "Integer", paramType="query"),
            @ApiImplicitParam(name = "userName", dataType = "String", paramType="query"),
            @ApiImplicitParam(name = "dept", dataType = "entity", paramType="body")
    })
    @CheckParams({
            @CheckParam(value = Check.NotNull, argName = "userId", msg = "你大爷的，这个是必填参数！"),
            @CheckParam(value = Check.NotEmpty, argName = "userName"),
            @CheckParam(value = Check.NotEmpty, argName = "dept.deptName"),
            @CheckParam(value = Check.Past, argName = "dept.createTime"),
            @CheckParam(value = Check.lt, argName = "dept.employees", express = "2") // 对集合的size判断
    })
    @PostMapping("/entityMultiCheck")
    public Object entityMultiCheck(Integer userId, String userName, @RequestBody DeptEntity dept) {
        System.err.println(userId);
        System.err.println(userName);
//        System.err.println(dept.getDeptName());
        return 1;
    }
}
