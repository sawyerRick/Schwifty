package cn.sawyer.schwifty.web;

import cn.sawyer.schwifty.web.core.SchwiftyApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

/**
 * @program: schwifty
 * @description:
 * @author: sawyer
 * @create: 2020-03-01 19:38
 **/
public class App{
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws ClassNotFoundException {
//        logger.info("cool");
//        System.out.println("");
        SchwiftyApplication.run(App.class);
    }
}