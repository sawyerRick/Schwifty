package cn.sawyer.schwifty.web.annotation.route;

/**
 * @program: schwifty
 * @description: Post请求路径。类似PostMapping
 * @author: sawyer
 * @create: 2020-03-01 19:49
 **/

public @interface PostSchwifty {
    String value() default "";
}
