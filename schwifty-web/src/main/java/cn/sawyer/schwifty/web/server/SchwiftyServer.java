package cn.sawyer.schwifty.web.server;

import cn.sawyer.schwifty.web.annotation.route.Schwifty;
import cn.sawyer.schwifty.web.init.SchwiftyServerInit;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: schwifty
 * @description: 基于Netty的Schwifty Server，非常地SCRATCH
 * @author: sawyer
 * @create: 2020-03-02 22:33
 **/
@Slf4j
public class SchwiftyServer {

    public static void main(String[] args) {
        SchwiftyServer schwiftyServer = SchwiftyServer.getInstance();
        schwiftyServer.start();
    }

    private static SchwiftyServer server;

    private EventLoopGroup boss = new NioEventLoopGroup();
    private EventLoopGroup worker = new NioEventLoopGroup();

    // 单例
    public static SchwiftyServer getInstance() {
        if (server == null) {
            synchronized (SchwiftyServer.class) {
                if (server == null) {
                    server = new SchwiftyServer();
                }
            }
        }

        return server;
    }

    private void init() {
        ServerBootstrap boot = new ServerBootstrap();
        boot.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new SchwiftyServerInit());

        try {
            ChannelFuture future = boot.bind(8080).sync(); // 启动

            System.err.println("Open your web browser and navigate to " +
                    "http" + "://127.0.0.1:" + 8080 + '/');

            future.channel().closeFuture().sync(); // 异步监听关闭

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }


    /**
     * Let's start! Get Schwifty!
     */
    public void start() {
        init();
    }
}
