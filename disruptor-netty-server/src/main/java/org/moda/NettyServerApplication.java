package org.moda;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import org.moda.consumer.ServerMessageConsumer;
import org.moda.disruptor.MessageConmuser;
import org.moda.disruptor.RingBufferWorkerPoolFactory;
import org.moda.server.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.MessageFormat;

/**
 * @author : MODA-Master
 * @Title : NettyServerApplication
 * @ProjectName disruptor-netty
 * @Description : TODO
 * @Time : Created in 2020/2/22 16:02
 * @Modifyed By :
 */
@SpringBootApplication
public class NettyServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(NettyServerApplication.class, args);

        // 1.创建 disruptor 的 组件
        MessageConmuser[] conmusers = new ServerMessageConsumer[10];
        for (int i = 0; i < conmusers.length; i++) {
            conmusers[i] = new ServerMessageConsumer(MessageFormat.format("disruptor::server-conmuser::{0}", i));
        }
        RingBufferWorkerPoolFactory.getInstance().initAndStart(ProducerType.MULTI,
                1024 * 1024, new BlockingWaitStrategy(), conmusers);

        // 2.启动netty
        NettyServer.newInstance();
    }
}
