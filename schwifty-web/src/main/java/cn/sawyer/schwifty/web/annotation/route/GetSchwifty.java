package cn.sawyer.schwifty.web.annotation.route;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: schwifty
 * @description: Get请求路径。类似GetMapping
 * @author: sawyer
 * @create: 2020-03-01 19:48
 **/

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GetSchwifty {

    String value() default "";
}
