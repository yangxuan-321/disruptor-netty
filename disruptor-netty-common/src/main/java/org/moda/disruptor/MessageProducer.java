package org.moda.disruptor;

import com.lmax.disruptor.RingBuffer;
import io.netty.channel.ChannelHandlerContext;
import org.moda.disruptor.event.TransDataEvent;
import org.moda.dto.TransDataDTO;

/**
 * @author : MODA-Master
 * @Title : MessageProducer
 * @ProjectName disruptor-netty
 * @Description : 生产者
 * @Time : Created in 2020/2/24 19:57
 * @Modifyed By :
 */
public class MessageProducer {

    private String producerId;

    private RingBuffer<TransDataEvent> ringBuffer;

    public MessageProducer(){

    }

    public MessageProducer(String producerId, RingBuffer<TransDataEvent> ringBuffer) {
        this.producerId = producerId;
        this.ringBuffer = ringBuffer;
    }

    public void sendData(TransDataDTO dataDTO, ChannelHandlerContext ctx){
        //1. 发送消息, 首先我们从我们的RingBuffer里面获取一个可用的序号
        long sequence = ringBuffer.next(); //sequence

        try {
            //2. 根据这个序号，找到具体的OrderEvent元素
            //   此时获取的 event 对象，是没有被填充的(属性未被赋值)
            TransDataEvent event = ringBuffer.get(sequence);

            //3. 填充对象
            event.setDataDTO(dataDTO);
            event.setCtx(ctx);
        }finally {
            //4. 提交(发布)操作
            ringBuffer.publish(sequence);
        }

    }
}
