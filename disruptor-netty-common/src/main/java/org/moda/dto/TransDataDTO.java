package org.moda.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : MODA-Master
 * @Title : TransDataDTO
 * @ProjectName disruptor-netty
 * @Description : TODO
 * @Time : Created in 2020/2/22 16:04
 * @Modifyed By :
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransDataDTO implements Serializable {

    private static final long serialVersionUID = 8763561286199081881L;

    // id
    private String id;
    // 名称
    private String name;
    // 文本内容
    private String message;
}
