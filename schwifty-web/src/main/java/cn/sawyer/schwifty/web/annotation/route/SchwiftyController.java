package cn.sawyer.schwifty.web.annotation.route;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: schwifty
 * @description: SchwiftyController，类标记了它，就成为了Controller。类似RestController
 * @author: sawyer
 * @create: 2020-03-01 19:50
 **/

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SchwiftyController {
    String value() default "/";
}
