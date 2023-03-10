package com.tzj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName MainPro
 * @Description TODO
 * @Author QiBin
 * @Date 2022/2/2311:38
 * @Version 1.0
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MainPro {

    String num;
    String name;
    String unit;
    String pro;
    String flag;

}
