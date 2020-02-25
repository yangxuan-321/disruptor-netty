package org.moda.consumer;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import org.moda.disruptor.MessageConmuser;
import org.moda.disruptor.event.TransDataEvent;
import org.moda.dto.TransDataDTO;

/**
 * @author : MODA-Master
 * @Title : ServerMessageConsumer
 * @ProjectName disruptor-netty
 * @Description : 客户端的消费者
 * @Time : Created in 2020/2/24 21:27
 * @Modifyed By :
 */
public class ClientMessageConsumer extends MessageConmuser {

    public ClientMessageConsumer(){

    }

    public ClientMessageConsumer(String consumerId){
        this.consumerId = consumerId;
    }

    @Override
    public void onEvent(TransDataEvent event) throws Exception {
        TransDataDTO res = event.getDataDTO();
        try {
            ChannelHandlerContext ctx = event.getCtx();
            // 1.业务逻辑处理
            System.out.println(JSON.toJSON(res).toString());
        }finally {
            // 释放
            ReferenceCountUtil.release(res);
        }
    }
}
