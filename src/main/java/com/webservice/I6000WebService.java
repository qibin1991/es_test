package com.webservice;


import org.springframework.stereotype.Service;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @WebService 使接口为webService接口
 * @name 暴露服务名称
 * @targetNamespace 命名空间, 一般是接口的包名倒序
 **/
@Service
@WebService(name = "getKPIValue", targetNamespace = "http://webservice")
public interface I6000WebService{

    @WebMethod
    public void getKPIValue(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,@WebParam(name = "CorporationCode") String corporationCode, @WebParam(name = "Time") String time, @WebParam(name = "name") String names);

}

