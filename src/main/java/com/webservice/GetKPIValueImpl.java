package com.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import javax.jws.WebService;
import java.util.HashMap;
import java.util.Map;


/**
 * @WebService 使接口为webService接口
 * @serviceName 与接口中指定的name一致
 * @targetNamespace 命名空间, 一般是接口的包名倒序
 * @endpointInterface 接口地址
 **/

@WebService(serviceName = "GetKPIValue",targetNamespace = "http://webservice",endpointInterface = "webservice.GetKPIValue")
public class GetKPIValueImpl extends DsbaseController implements GetKPIValue {

    @Autowired
    public I6000Service i6000Service;

    @Override
    public String sendMessage(String corporationCode, String time, String names) {


        //返回值
        Map<String, Object> responseMap = new HashMap<String, Object>();

        String message = "";
        String status = "success";
        String reason = "";
        Map<String, Object>[] rollbackSize = new HashMap[1];
        Map<String, Object> kpiNameMap = new HashMap<String, Object>();
        rollbackSize[0] = kpiNameMap;
        responseMap.put("kpis",rollbackSize);

        try {
            //判断单位code  22位重庆单位code
            if("22".equals(corporationCode)){
                String[] kpinameArray = names.split(",");
                if(kpinameArray.length > 0){
                    for (int i = 0; i < kpinameArray.length; i++) {
                        if("BusinessUserRegNum".equals(kpinameArray[i])){
                            //注册用户数
                            Number toUserTotal = (Number) i6000Service.toUserTotal();
                            kpiNameMap.put("BusinessUserRegNum",toUserTotal.intValue());
                        }else if ("BusinessSystemOnlineNum".equals(kpinameArray[i])){
                            //在线用户数
                            Number toOnlineUserTotal = (Number) i6000Service.toOnlineUserTotal();
                            kpiNameMap.put("BusinessSystemOnlineNum",toOnlineUserTotal.intValue());
                        }else if ("BusinessDayLoginNum".equals(kpinameArray[i])){
                            //日登录人数
                            Number toDayUserTotal = (Number) i6000Service.toDayUserTotal();
                            kpiNameMap.put("BusinessDayLoginNum",toDayUserTotal.intValue());
                        }else if ("BusinessVisitCount".equals(kpinameArray[i])){
                            //累计访问人次
                            Number requestPersonTotal = (Number)i6000Service.requestPersonTotal();
                            kpiNameMap.put("BusinessVisitCount",requestPersonTotal);
                        }else if ("BusinessSystemSessionNum".equals(kpinameArray[i])){
                            //页面会话连接数
                            kpiNameMap.put("BusinessSystemSessionNum", SessionListener.userCount.incrementAndGet());
                        }else if ("BusinessSystemResponseTime".equals(kpinameArray[i])){
                            //系统服务响应时长
//                            String interfaceTotalTime = (String) RedisUtil.getStrValue1("interfaceTotalTime");
//                            kpiNameMap.put("BusinessSystemResponseTime",interfaceTotalTime);
                        }else if ("BusinessSystemRunningTime".equals(kpinameArray[i])){
                            //系统健康运行时长
//                            long startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
//                            long presentTime = TimeUtil.getStamp() * 1000L;
//                            kpiNameMap.put("BusinessSystemRunningTime",(presentTime - startTime) / 1000);
                        }else if ("BusinessDataTableSpace".equals(kpinameArray[i])){
                            //业务应用系统占用表空间大小
                            Number toProgramOfChartTotal = (Number)i6000Service.toProgramOfChartTotal();
                            kpiNameMap.put("BusinessDataTableSpace",toProgramOfChartTotal.intValue());
                        }else if ("BusinessSystemDBTime".equals(kpinameArray[i])){
                            //数据库平均响应时长,无法统计

                        }else if ("TSAssetNumTotal".equals(kpinameArray[i])){
                            //累计终端台账数
                            Number terminalTotal = (Number) i6000Service.terminalTotal();
                            kpiNameMap.put("TSAssetNumTotal",terminalTotal.intValue());
                        }else if ("TSAssetUnknowNum".equals(kpinameArray[i])){
                            //未知终端数
                            Number noTerminalTotal = (Number) i6000Service.noTerminalTotal();
                            kpiNameMap.put("TSAssetUnknowNum",noTerminalTotal.intValue());
                        }else if ("TSAssetIncompleteNum".equals(kpinameArray[i])){
                            //终端重要信息缺失数量
                            Number addTerminalTotal = (Number) i6000Service.addTerminalTotal();
                            kpiNameMap.put("TSAssetIncompleteNum",addTerminalTotal.intValue());
                        }else if ("TSAlarmNumDay".equals(kpinameArray[i])){
                            //日终端告警数
                            Number toDayAlarmTotal = (Number) i6000Service.toDayAlarmTotal();
                            if(toDayAlarmTotal == null){
                                toDayAlarmTotal = 0;
                            }
                            kpiNameMap.put("TSAlarmNumDay",toDayAlarmTotal);
                        }else if ("TSAlarmNumMonth".equals(kpinameArray[i])){
                            //月终端告警数
                            Number toMonthAlarmTotal = (Number) i6000Service.toMonthAlarmTotal();
                            if(toMonthAlarmTotal == null){
                                toMonthAlarmTotal = 0;
                            }
                            kpiNameMap.put("TSAlarmNumMonth",toMonthAlarmTotal);
                        }else if ("TSAlarmNumTotal".equals(kpinameArray[i])){
                            //累计终端告警数
                            Number toAlarmTotal = (Number) i6000Service.toAlarmTotal();
                            kpiNameMap.put("TSAlarmNumTotal",toAlarmTotal);
                        }else if ("TSAssetUnknowRate".equals(kpinameArray[i])){
                            //未知终端占比
                            Number noTerminalTotal = (Number) i6000Service.noTerminalTotal();
                            Number terminalTotal = (Number) i6000Service.terminalTotal();
                            String noTerminalOfTerminalTotal = String.format("%.2f", ((double)noTerminalTotal.intValue()/terminalTotal.intValue())*100);
                            kpiNameMap.put("TSAssetUnknowRate",noTerminalOfTerminalTotal);
                        }else if ("TSAssetIncompleteRate".equals(kpinameArray[i])){
                            //终端重要信息缺失占比
                            Number addTerminalTotal = (Number) i6000Service.addTerminalTotal();
                            Number terminalTotal = (Number) i6000Service.terminalTotal();
                            String addTerminalOfTerminalTotal = String.format("%.2f", ((double)addTerminalTotal.intValue()/terminalTotal.intValue())*100);
                            kpiNameMap.put("TSAssetIncompleteRate",addTerminalOfTerminalTotal);
                        }else if ("TSAlarmAssetRateDay".equals(kpinameArray[i])){
                            //日告警终端占比
                            Number toDayAlarmTotal = (Number) i6000Service.toDayAlarmTerminalTotal();
                            Number terminalTotal = (Number) i6000Service.terminalTotal();
                            String toDayAlarmTerminalOfTerminalTotal = String.format("%.2f", ((double)toDayAlarmTotal.intValue()/terminalTotal.intValue())*100);
                            kpiNameMap.put("TSAlarmAssetRateDay",toDayAlarmTerminalOfTerminalTotal);
                        }else if ("TSAlarmAssetRateMonth".equals(kpinameArray[i])){
                            //月告警终端占比
                            Number toMonthAlarmTotal = (Number) i6000Service.toMonthAlarmTerminalTotal();
                            Number terminalTotal = (Number) i6000Service.terminalTotal();
                            String toMonthAlarmTerminalOfTerminalTotal = String.format("%.2f", ((double)toMonthAlarmTotal.intValue()/terminalTotal.intValue())*100);
                            kpiNameMap.put("TSAlarmAssetRateMonth",toMonthAlarmTerminalOfTerminalTotal);
                        }
                    }
                }else {
                    message = "传入的kpiName为空";
                    reason = "传入的kpiName为空";
                }
            }else {
                message = "传入的单位Code错误，传入单位Code为:" + corporationCode;
                reason = "传入的单位Code错误，传入单位Code为:" + corporationCode;
            }
        }catch (Exception e){
            status = "failure";
            message = "接口运行失败，请重试!";
            reason = "接口运行失败，请重试!";
            System.out.println(e.getStackTrace());
            logger.info("I6000接口报错,错误为:" + e);
        }

        responseMap.put("status",status);
        responseMap.put("message",message);
        responseMap.put("reason",reason);
        return responseMap.toString();
    }
}
