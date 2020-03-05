package cn.sawyer.schwifty.web.annotation.boot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: schwifty
 * @description: 核心启动类
 * @author: sawyer
 * @create: 2020-03-01 20:07
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SchwiftyBootApplication {
}
