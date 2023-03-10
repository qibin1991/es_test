package com.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;

/**
 * @ClassName HttpClient
 * @Description TODO
 * @Author QiBin
 * @Date 2022/6/23 16:23
 * @Version 1.0
 **/

/**
 * Channel、ChannelPipeline、ChannelHandler、ChannelHandlerContext 之间的关系
 * 在编写 Netty 程序时，经常跟我们打交道的是上面这几个对象，这也是 Netty 中几个重要的对象，下面我们来看看它们之间有什么样的关系。
 *
 * Netty 中的 Channel 是框架自己定义的一个通道接口，Netty 实现的客户端 NIO 套接字通道是 NioSocketChannel，提供的服务器端 NIO 套接字通道是 NioServerSocketChannel。
 *
 * 当服务端和客户端建立一个新的连接时， 一个新的 Channel 将被创建，同时它会被自动地分配到它专属的 ChannelPipeline。
 *
 * ChannelPipeline 是一个拦截流经 Channel 的入站和出站事件的 ChannelHandler 实例链，并定义了用于在该链上传播入站和出站事件流的 API。那么就很容易看出这些 ChannelHandler 之间的交互是组成一个应用程序数据和事件处理逻辑的核心。
 *
 *
 *
 * 上图描述了 IO 事件如何被一个 ChannelPipeline 的 ChannelHandler 处理的。
 *
 * ChannelHandler分为 ChannelInBoundHandler 和 ChannelOutboundHandler 两种，如果一个入站 IO 事件被触发，这个事件会从第一个开始依次通过 ChannelPipeline中的 ChannelInBoundHandler，先添加的先执行。
 *
 * 若是一个出站 I/O 事件，则会从最后一个开始依次通过 ChannelPipeline 中的 ChannelOutboundHandler，后添加的先执行，然后通过调用在 ChannelHandlerContext 中定义的事件传播方法传递给最近的 ChannelHandler。
 *
 * 在 ChannelPipeline 传播事件时，它会测试 ChannelPipeline 中的下一个 ChannelHandler 的类型是否和事件的运动方向相匹配。
 *
 * 如果某个ChannelHandler不能处理则会跳过，并将事件传递到下一个ChannelHandler，直到它找到和该事件所期望的方向相匹配的为止。
 */


public class HttpClient {

    public static void main(String[] args) throws Exception {
        String host = "127.0.0.1";
        int port = 8080;

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new HttpClientCodec());
                            pipeline.addLast(new HttpObjectAggregator(65536));
                            pipeline.addLast(new HttpClientHandler());
                        }
                    });

            // 启动客户端.
            ChannelFuture f = b.connect(host, port).sync();
            f.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully();
        }
    }

}
