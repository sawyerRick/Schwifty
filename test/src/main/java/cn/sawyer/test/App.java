package cn.sawyer.test;

import cn.sawyer.schwifty.web.annotation.boot.SchwiftyBootApplication;
import cn.sawyer.schwifty.web.core.SchwiftyApplication;

/**
 * @program: schwifty
 * @description:
 * @author: sawyer
 * @create: 2020-03-02 14:54
 **/
@SchwiftyBootApplication
public class App {
    public static void main(String[] args) {
        SchwiftyApplication.run(App.class, args);
    }
}
