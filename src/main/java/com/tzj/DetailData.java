package com.tzj;

import com.deepoove.poi.data.RowRenderData;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @ClassName DetailData
 * @Description TODO
 * @Author QiBin
 * @Date 2022/2/2210:09
 * @Version 1.0
 **/
@Data
@Builder
public class DetailData {

    //燃料燃烧
    private List<RowRenderData> burns;

    //生产输入
    private List<RowRenderData> processInputs;
    //生产输出
    private List<RowRenderData> processOutputs;
    //总消费量
    private List<RowRenderData> consumption;




    public DetailData() {
    }

    public DetailData(List<RowRenderData> burns, List<RowRenderData> processInputs, List<RowRenderData> processOutputs, List<RowRenderData> consumption) {
        this.burns = burns;
        this.processInputs = processInputs;
        this.processOutputs = processOutputs;
        this.consumption = consumption;
    }
}
