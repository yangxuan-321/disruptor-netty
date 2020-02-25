package org.moda.consumer;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import org.moda.disruptor.MessageConmuser;
import org.moda.disruptor.MessageProducer;
import org.moda.disruptor.event.TransDataEvent;
import org.moda.dto.TransDataDTO;

/**
 * @author : MODA-Master
 * @Title : ServerMessageConsumer
 * @ProjectName disruptor-netty
 * @Description : TODO
 * @Time : Created in 2020/2/24 21:27
 * @Modifyed By :
 */
public class ServerMessageConsumer extends MessageConmuser {

    public ServerMessageConsumer(){

    }

    public ServerMessageConsumer(String consumerId){
        this.consumerId = consumerId;
    }

    @Override
    public void onEvent(TransDataEvent event) throws Exception {

        TransDataDTO req = event.getDataDTO();
        ChannelHandlerContext ctx = event.getCtx();

        // 1.业务逻辑处理
        //数据库持久化操作 IO读写 ---> 交给一个线程池 去异步的调用执行
        System.out.println("server receive:" + JSON.toJSON(req).toString());

        // 2.回送响应信息
        TransDataDTO res = new TransDataDTO();
        res.setId(req.getId());
        res.setName(req.getName());
        res.setMessage(req.getMessage());

        // 写出 并且 刷新到 NIO通道上
        // 请看 client 上 读取完消息 释放buffer。但是server却不释放。
        // 是因为 在随后使用 writeAndFlush 写方法时，会帮忙释放buffer
        // 可以看看 DefaultChannelPipeline 方法。
        ctx.writeAndFlush(res);
    }
}
