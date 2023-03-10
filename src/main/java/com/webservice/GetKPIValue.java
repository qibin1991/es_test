package com.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;


/**
 * @WebService 使接口为webService接口
 * @name 暴露服务名称
 * @targetNamespace 命名空间, 一般是接口的包名倒序
 **/
@WebService(name = "GetKPIValue", targetNamespace = "http://service.system.xybc.com")
public interface GetKPIValue {

    @WebMethod
    public String sendMessage(@WebParam(name = "CorporationCode") String corporationCode, @WebParam(name = "Time") String time, @WebParam(name = "name") String names);

}

