package org.moda.handler;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.moda.disruptor.MessageProducer;
import org.moda.disruptor.RingBufferWorkerPoolFactory;
import org.moda.dto.TransDataDTO;

/**
 * @author : MODA-Master
 * @Title : ClientHandler
 * @ProjectName disruptor-netty
 * @Description : TODO
 * @Time : Created in 2020/2/23 15:06
 * @Modifyed By :
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (!(msg instanceof TransDataDTO)){
                return;
            }

            // 服务器 响应回来的信息
            TransDataDTO res = (TransDataDTO)msg;

            // 拿到生产者
            String producerId = "disruptor::client-producer::dbstore";
            MessageProducer messageProducer = RingBufferWorkerPoolFactory.getInstance().getMessageProducer(producerId);

            // 投递数据
            messageProducer.sendData(res, ctx);
        }finally {
            // buffer 用完了 要做回收。
//            ReferenceCountUtil.release(msg);
        }
    }
}
