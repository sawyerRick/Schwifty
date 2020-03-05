package cn.sawyer.schwifty.web.annotation.route;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: schwifty
 * @description: Controller路由，类似SpringMvc的RequestMapping()
 * @author: sawyer
 * @create: 2020-03-01 19:43
 **/

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Schwifty {
    String value() default "/";
}
