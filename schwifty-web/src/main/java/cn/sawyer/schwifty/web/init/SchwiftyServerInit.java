package cn.sawyer.schwifty.web.init;

import cn.sawyer.schwifty.web.handler.SchwiftyHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @program: schwifty
 * @description:
 * @author: sawyer
 * @create: 2020-03-02 22:41
 **/
public class SchwiftyServerInit extends ChannelInitializer {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new HttpServerCodec()); // HTTP codec

        ch.pipeline().addLast(new SchwiftyHandler()); // My handler
    }
}
