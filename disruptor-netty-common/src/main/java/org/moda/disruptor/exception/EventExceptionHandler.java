package org.moda.disruptor.exception;

import com.lmax.disruptor.ExceptionHandler;
import org.moda.disruptor.event.TransDataEvent;

/**
 * @author : MODA-Master
 * @Title : EventExceptionHandler
 * @ProjectName disruptor-netty
 * @Description : 异常处理类
 * @Time : Created in 2020/2/24 20:25
 * @Modifyed By :
 */
public class EventExceptionHandler implements ExceptionHandler<TransDataEvent> {

    @Override
    public void handleEventException(Throwable ex, long sequence, TransDataEvent event) {

    }

    @Override
    public void handleOnStartException(Throwable ex) {

    }

    @Override
    public void handleOnShutdownException(Throwable ex) {

    }
}
