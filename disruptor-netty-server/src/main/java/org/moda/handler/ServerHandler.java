package org.moda.handler;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.moda.disruptor.MessageProducer;
import org.moda.disruptor.RingBufferWorkerPoolFactory;
import org.moda.dto.TransDataDTO;

/**
 * @author : MODA-Master
 * @Title : ServerHandler
 * @ProjectName disruptor-netty
 * @Description : TODO
 * @Time : Created in 2020/2/23 11:56
 * @Modifyed By :
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    // 重写 读取数据 的方法
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 经过 前两个 编解码的 处理器，此时的 msg 对象已经 是可以使用的了。直接强转
        if (!(msg instanceof TransDataDTO)){
            return;
        }
        // 强转
        TransDataDTO req = (TransDataDTO)msg;
        // 数据库持久化操作 IO读写 ---> 交给一个线程池 去异步的调用执行

        // 拿到生产者
        String producerId = "disruptor::server-producer::dbstore";
        MessageProducer messageProducer = RingBufferWorkerPoolFactory.getInstance().getMessageProducer(producerId);

        // 投递数据
        messageProducer.sendData(req, ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(new TransDataDTO("1", "2", "3"));
    }
}
