package cn.sawyer.schwifty.web.enums;

/**
 * @program: schwifty
 * @description:
 * @author: sawyer
 * @create: 2020-03-03 11:17
 **/
public enum RespType {

    STATIC(0, "静态资源"),
    JSON(1, "JSON"),
    TEXT(2, "TEXT");

    Integer value;

    String desc;

    RespType(Integer v, String d) {
        value = v;
        this.desc = d;
    }
}
