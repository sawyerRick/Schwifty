package cn.sawyer.schwifty.web.method;

import cn.sawyer.schwifty.web.annotation.route.GetSchwifty;
import cn.sawyer.schwifty.web.annotation.route.SchwiftyController;
import org.slf4j.LoggerFactory;

/**
 * @program: schwifty
 * @description: 测试反射调用方法。
 * @author: sawyer
 * @create: 2020-03-02 23:15
 **/

@SchwiftyController
public class TestController {


    // 测试JSON，TEXT
    @GetSchwifty("ding")
    public String getDing(String idx) {
        System.out.println("[+] from ding... idx:" + idx);

        return "Get Schwifty!";
    }

    // 测试静态文件
    @GetSchwifty("index")
    public String getIdx(String arg1, String arg2) {

        return "index.html";
    }
}
