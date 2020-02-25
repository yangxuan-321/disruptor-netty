package org.moda.disruptor.event;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.moda.dto.TransDataDTO;

/**
 * @author : MODA-Master
 * @Title : TransDataEvent
 * @ProjectName disruptor-netty
 * @Description : TODO
 * @Time : Created in 2020/2/24 20:16
 * @Modifyed By :
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransDataEvent {
    private TransDataDTO dataDTO;

    private ChannelHandlerContext ctx;
}
