package cn.sawyer.schwifty.web.handler;

import cn.sawyer.schwifty.web.annotation.route.GetSchwifty;
import cn.sawyer.schwifty.web.annotation.route.SchwiftyController;
import cn.sawyer.schwifty.web.domain.RouteInfo;
import cn.sawyer.schwifty.web.enums.RespType;
import cn.sawyer.schwifty.web.method.TestController;
import com.alibaba.fastjson.JSON;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.*;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
/**
 * @program: schwifty
 * @description:
 * @author: sawyer
 * @create: 2020-03-02 23:07
 **/
@Slf4j
public class SchwiftyHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * url-routeInfo
     */
    private static Map<String, RouteInfo> routeMap = new ConcurrentHashMap<>();

    public SchwiftyHandler() {
        RouteInfo jsRoute = testJsonRoute();
        RouteInfo staticRoute = testStaticMethod();
        System.out.println("路由注册：");
        System.out.println(jsRoute);
        System.out.println(staticRoute);
        routeMap.put(jsRoute.getRoute(), jsRoute);
        routeMap.put(staticRoute.getRoute(), staticRoute);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest) {
            log.info("New read:");
            HttpRequest req = (HttpRequest) msg;

            boolean keepAlive = HttpUtil.isKeepAlive(req);

            String visitUri = req.uri();
            System.out.println("uri:" + visitUri);
            byte[] result = null;
            // 搜索路由
            if (routeMap.containsKey(visitUri)) {
                RouteInfo routeInfo = routeMap.get(visitUri);
                RespType type = routeInfo.getType();
                log.info("类型：" + type);

                switch (type) {
                    case JSON:
                        result = jsonHandler(req, routeInfo);
                        break;
                    case TEXT:
                        result = testHandler(req, routeInfo);
                        break;
                    case STATIC:
                        result = staticHandler(req, routeInfo);
                        break;
                }
            }
            result = result == null ? "Nothing here".getBytes() : result;
            FullHttpResponse resp = new DefaultFullHttpResponse(req.protocolVersion(), OK, Unpooled.copiedBuffer(result));

            // 配置长连接
            if (keepAlive) {
                if (!req.protocolVersion().isKeepAliveDefault()) {
                    resp.headers().set(CONNECTION, KEEP_ALIVE);
                }
            } else {
                resp.headers().set(CONNECTION, CLOSE);
            }

            // json头
//            resp.headers().set(CONTENT_TYPE, APPLICATION_JSON)
//                    .setInt(CONTENT_LENGTH, resp.content().readableBytes());
            // 静态文件头
            resp.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8")
                    .setInt(CONTENT_LENGTH, resp.content().readableBytes());

            ChannelFuture future = ctx.writeAndFlush(resp);
            if (!keepAlive) {
                future.addListener(ChannelFutureListener.CLOSE);
                System.out.println("不是长连接，关闭！");
            }
        }

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("Closed by peer...");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    // 测试json，text方法。
    public RouteInfo testJsonRoute() {
        try {
            // controller 路由
            String route = TestController.class.getAnnotation(SchwiftyController.class).value();

            TestController dingController = TestController.class.newInstance();

            // 方法路由
            String subRoute;
            Method dingMethod = TestController.class.getMethod("getDing", String.class);
            subRoute = dingMethod.getAnnotation(GetSchwifty.class).value();
            route += subRoute;

            RouteInfo routeInfo = RouteInfo.builder()
                    .route(route)
                    .method(dingMethod)
                    .controller(dingController)
                    .type(RespType.JSON)
                    .build();

            return routeInfo;
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    // 测试静态路由方法
    public RouteInfo testStaticMethod() {
        try {
            // controller 路由
            String route = TestController.class.getAnnotation(SchwiftyController.class).value();
            TestController testController = TestController.class.newInstance();

            // 方法路由
            String subRoute;
            Method idxMethod = TestController.class.getMethod("getIdx", String.class, String.class);
            subRoute = idxMethod.getAnnotation(GetSchwifty.class).value();
            route += subRoute;

            RouteInfo routeInfo = RouteInfo.builder()
                    .route(route)
                    .method(idxMethod)
                    .controller(testController)
                    .type(RespType.STATIC)
                    .build();

            return routeInfo;
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    private byte[] invoke(RouteInfo routeInfo, String... args) {
        String visitUri = routeInfo.getRoute();
        Method method = routeMap.get(visitUri).getMethod();
        Object controller = routeMap.get(visitUri).getController();
        Object resp = null;
        try {
            resp = method.invoke(controller, args);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resp.toString().getBytes();
    }

    // 处理静态路由
    private byte[] staticHandler(HttpRequest req, RouteInfo routeInfo) {
        System.out.println("静态");

        Method method = routeInfo.getMethod();
        Object controller = routeInfo.getController();
        try {
            String fileName = "/" + method.invoke(controller,"","").toString();
            log.info("file name:" + fileName);
            String path = this.getClass().getResource(fileName).getPath();
            log.info("path:" + path);
            byte[] buffer = Files.readAllBytes(new File(path).toPath());

            return buffer;
        } catch (Exception e) {
            log.error("调用方法/读文件错误");
            e.printStackTrace();
        }

        return null;
    }

    // 处理json
    private byte[] jsonHandler(HttpRequest req, RouteInfo routeInfo) {

        Method method = routeInfo.getMethod();
        String uri = routeInfo.getRoute();
        Object controller = routeInfo.getController();
        Object resp;
        try {
            resp = method.invoke(controller, "here");

            return JSON.toJSONBytes(resp);

        } catch (Exception e) {
            log.error("invoke错误:" + routeInfo);
            e.printStackTrace();
        }

        return "Error occur...".getBytes();
    }

    // 处理text
    private byte[] testHandler(HttpRequest req, RouteInfo routeInfo) {

        return null;
    }
}