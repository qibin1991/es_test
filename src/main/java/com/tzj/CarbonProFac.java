package com.tzj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName CarbonProFac
 * @Description TODO
 * @Author QiBin
 * @Date 2022/2/2311:18
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarbonProFac {

    String carbon;

    String name;

    String level;

    String count;

}
