package org.moda.disruptor;

import com.lmax.disruptor.WorkHandler;
import org.moda.disruptor.event.TransDataEvent;

/**
 * @author : MODA-Master
 * @Title : MessageConmuser
 * @ProjectName disruptor-netty
 * @Description : 不清楚 具体的消费者是谁
 * @Time : Created in 2020/2/24 20:10
 * @Modifyed By :
 */

public abstract class MessageConmuser implements WorkHandler<TransDataEvent> {
    protected String consumerId;

    public MessageConmuser(){

    }

    public MessageConmuser(String consumerId){
        this.consumerId = consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getConsumerId() {
        return consumerId;
    }
}
