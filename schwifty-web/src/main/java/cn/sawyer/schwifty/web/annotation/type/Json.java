package cn.sawyer.schwifty.web.annotation.type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: schwifty
 * @description: 表示该接口返回JSON，类似ResponseBody
 * @author: sawyer
 * @create: 2020-03-02 23:11
 **/

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Json {
}
