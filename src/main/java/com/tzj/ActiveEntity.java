package com.tzj;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName ActiveEntity
 * @Description TODO
 * @Author QiBin
 * @Date 2022/2/2116:54
 * @Version 1.0
 **/
@Data
@Builder
public class ActiveEntity {
    /**
     * 第一列 name
     */
    String name="/";
    /**
     * 品种 或 种类
     */
    String variety="/";
    /**
     * 消耗或者产量来源说明
     */
    String consumeInfo="/";
    /**
     * 低位发热来源说明
     */
    String burnInfo="/";


}
