package org.moda.client;

import cn.hutool.core.util.IdUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.moda.code.MarshallingCodeCFactory;
import org.moda.dto.TransDataDTO;
import org.moda.handler.ClientHandler;

import java.util.List;

/**
 * @author : MODA-Master
 * @Title : NettyClient
 * @ProjectName disruptor-netty
 * @Description : TODO
 * @Time : Created in 2020/2/23 14:14
 * @Modifyed By :
 */
public class NettyClient {

    public static final String host = "127.0.0.1";
    public static final int port = 8765;

    private Channel channel;    // 如果 对应 不同的 端口 请维护 不同的 channel。 池化操作。 ConCurrentHashMap

    private EventLoopGroup work;

    private ChannelFuture cf;

    public static NettyClient newInstance() {
        NettyClient client = new NettyClient();
        return client;
    }
    
    private NettyClient(){
        this.connect(host, port);
    }

    private void connect(String host, int port) {
        /**
         * 1.创建1个工作线程组
         * 一个用于业务处理
         */
        // 业务处理线程
        this.work = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        try {
            bootstrap.group(work)
            .channel(NioSocketChannel.class)
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
            .handler(new ChannelInitializer<NioSocketChannel>() {
                // 回调
                @Override
                protected void initChannel(NioSocketChannel sc) throws Exception {
                    // 向管道加一些东西
                    // addLast往后接
                    sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                    sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                    sc.pipeline().addLast(new ClientHandler());
                }
            });
            // 绑定8765端口，同步请求等待连接
            this.cf = bootstrap.connect(host, port).sync();
            System.out.println("client collect...");

            // 获取通道 （用于发送数据）
            this.channel = cf.channel();

//            cf.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // 关闭 相关线程组
            // 优雅关闭
//            work.shutdownGracefully();
        }
    }

    private void send(){
        TransDataDTO req = new TransDataDTO(IdUtil.simpleUUID(), "name", "message");
        System.out.println("client start send...");
        this.channel.writeAndFlush(req);
    }

    public void send(TransDataDTO dto){
        send();
    }

    public void send(List<TransDataDTO> dtos){
        for (int i = 0; i < 5; i++) {
            send();
        }
    }

    public void send(TransDataDTO... dtos){

    }

    public void close() throws Exception{
        if (null != cf){
            cf.channel().closeFuture().sync();
        }

        if (null != work){
            work.shutdownGracefully();
        }

        System.out.println("client ShutDown...");
    }
}
