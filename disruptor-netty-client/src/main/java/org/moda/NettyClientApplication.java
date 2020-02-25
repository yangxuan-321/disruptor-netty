package org.moda;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import org.moda.client.NettyClient;
import org.moda.consumer.ClientMessageConsumer;
import org.moda.disruptor.MessageConmuser;
import org.moda.disruptor.RingBufferWorkerPoolFactory;
import org.moda.dto.TransDataDTO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.MessageFormat;
import java.util.List;

/**
 * @author : MODA-Master
 * @Title : NettyServerApplication
 * @ProjectName disruptor-netty
 * @Description : TODO
 * @Time : Created in 2020/2/22 16:02
 * @Modifyed By :
 */
@SpringBootApplication
public class NettyClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(NettyClientApplication.class, args);

        // 1.创建 disruptor 的 组件
        MessageConmuser[] conmusers = new ClientMessageConsumer[10];
        for (int i = 0; i < conmusers.length; i++) {
            conmusers[i] = new ClientMessageConsumer(MessageFormat
                    .format("disruptor::client-conmuser::{0}", i));
        }
        RingBufferWorkerPoolFactory.getInstance().initAndStart(ProducerType.MULTI,
                1024 * 1024, new BlockingWaitStrategy(), conmusers);

        // 建立连接
        NettyClient client = NettyClient.newInstance();
        // 发送消息
        client.send((List<TransDataDTO>) null);
    }
}
