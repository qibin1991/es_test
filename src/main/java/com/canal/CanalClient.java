//package com.canal;
//
///**
// * @ClassName CanalClient
// * @Description TODO
// * @Author QiBin
// * @Date 2022/8/10 17:19
// * @Version 1.0
// **/
//import com.alibaba.fastjson.JSONObject;
//import com.alibaba.otter.canal.client.CanalConnector;
//import com.alibaba.otter.canal.client.CanalConnectors;
//import com.alibaba.otter.canal.protocol.CanalEntry;
//import com.alibaba.otter.canal.protocol.Message;
//import com.gmall.common.constansts.GmallConstants;
//import com.google.protobuf.ByteString;
//import com.google.protobuf.InvalidProtocolBufferException;
//
//import java.net.InetSocketAddress;
//import java.util.List;
//
//public class CanalClient {
//
//    public static void main(String[] args) throws InvalidProtocolBufferException {
//
//        //1.建立和canal的连接
//        CanalConnector canalConnector = CanalConnectors.newSingleConnector(new InetSocketAddress
//                ("hadoop102", 11111), "example", "", "");
//
//        canalConnector.connect();
//
//        //2.订阅cannal中指定的表的binlog数据
//        //只订阅gmall2021库下的所有表
//        canalConnector.subscribe("gmall2021.*");
//
//        //3.解析binglog数据
//        //3.1 从binglog中拉取message
//        while (true){
//
//            //从canal server中拉取100条数据
//            Message message = canalConnector.get(100);
//
//            //判断是否拿到了数据
//            if (message.getId() == -1){
//
//                try {
//                    System.out.println("当前没有数据，0.5s后再次尝试读取数据");
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                continue;
//            }
//
//            //拿到了数据，开始进行数据的解析工作
//            List<CanalEntry.Entry> entries = message.getEntries();
//
//            //每个entry都有entrytype，storevalue，tablename
//            //当前只读取entrytype是ROWDATA的entry，将符合条件的entry的storevalue反序列化
//            for (CanalEntry.Entry entry : entries) {
//
//                if (entry.getEntryType().equals(CanalEntry.EntryType.ROWDATA)){
//
//                    //获取storevalue
//                    ByteString storeValue = entry.getStoreValue();
//
//                    //反序列化
//                    CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(storeValue);
//
//                    //获取当前entry是对那个表的操作结果
//                    String tableName = entry.getHeader().getTableName();
//
//                    //对获取的数据进行处理
//                    handle(tableName, rowChange);
//
//                }
//
//            }
//
//        }
//
//    }
//
//
//    //只需要将order_info发生变化的数据，发送到kafka
//    //需要采集的是order_info当天生成的数据，update和delete不管
//    private static void handle(String tableName, CanalEntry.RowChange rowChange) {
//
//        if ("order_info".equals(tableName) && rowChange.getEventType().equals(CanalEntry.EventType.INSERT)){
//
//            //获取所有变化的行
//            List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();
//
//            //遍历所有行，取出列名和值，封装为json格式
//            for (CanalEntry.RowData rowData : rowDatasList) {
//
//                //4.以json格式，格式化数据
//                JSONObject jsonObject = new JSONObject();
//
//                //获取当前行，那些列发生变化后的结果
//                List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
//
//                for (CanalEntry.Column column : afterColumnsList) {
//
//                    jsonObject.put(column.getName(),column.getValue());
//
//                }
//
//                //5. 将数据发送到kafka
//                MyProducer.send(GmallConstants.KAFKA_TOPIC_NEW_ORDER, jsonObject.toString());
//
//            }
//
//        }
//
//    }
//
//}
