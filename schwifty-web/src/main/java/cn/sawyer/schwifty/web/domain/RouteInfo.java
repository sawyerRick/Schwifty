package cn.sawyer.schwifty.web.domain;

import cn.sawyer.schwifty.web.enums.RespType;
import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * @program: schwifty
 * @description: 路由反射信息
 * @author: sawyer
 * @create: 2020-03-02 23:25
 **/
@Data
@Builder
public class RouteInfo {

    String route;

    Method method;

    Object controller;

    RespType type;
}
