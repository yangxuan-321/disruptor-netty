package org.moda.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.moda.code.MarshallingCodeCFactory;
import org.moda.handler.ServerHandler;

/**
 * @author : MODA-Master
 * @Title : NettyServer
 * @ProjectName disruptor-netty
 * @Description : TODO
 * @Time : Created in 2020/2/22 16:03
 * @Modifyed By :
 */
public class NettyServer {

    public static NettyServer newInstance() {
        NettyServer server = new NettyServer();
        return server;
    }
    
    private NettyServer(){
        /**
         * 1.创建两个工作线程组
         * 一个用于接收网络请求
         * 一个用于业务处理
         */
        // 连接线程
        EventLoopGroup boos = new NioEventLoopGroup();
        // 业务处理线程
        EventLoopGroup work = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();

        try {
            bootstrap.group(boos, work)
            .channel(NioServerSocketChannel.class)
            // 连接队列大小设置
            .option(ChannelOption.SO_BACKLOG, 1024)
            // buffer大小设置，需要根据业务场景 每次传输 数据的大小 来定义。可以设置成自适应(但也会有性能的损耗),
            // 如果遇到 每次传输数据 差异太大的话，自适应就不一定 合适。
            .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
            // 缓冲的对象池，池化操作
            // 也就是说 buffer对象池。并不能说 每次用完对象就销毁，销毁和创建缓冲对象 也很损耗性能。
            // 缓冲区 池化操作
            .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
            // 日志打印
            .handler(new LoggingHandler(LogLevel.INFO))
            // 数据过来了，我应该给哪个方法去回调。回调的目的就是接受一下数据 异步 的去处理。
            // childHandler 就是 这样一个 异步回调的过程。
            // 回调 的 信息 是由 workgroup 线程组 去做实际处理的
            .childHandler(new ChannelInitializer<NioSocketChannel>() {
                // 回调
                @Override
                protected void initChannel(NioSocketChannel sc) throws Exception {
                    // 向管道加一些东西
                    // addLast往后接
                    sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                    sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                    sc.pipeline().addLast(new ServerHandler());
                }
            });
            // 绑定8765端口，同步请求等待连接
            ChannelFuture cf = bootstrap.bind(8765).sync();
            System.out.println("server startup");
            cf.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // 关闭 相关线程组
            // 优雅关闭
            boos.shutdownGracefully();
            work.shutdownGracefully();
            System.out.println("Sever ShutDown...");
        }
    }
}
