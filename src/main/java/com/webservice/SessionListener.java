package com.webservice;



import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

@WebListener
public class SessionListener implements HttpSessionListener, HttpSessionAttributeListener {


	
	public static AtomicInteger userCount = new AtomicInteger(0);
	
	SessionListener(){
		
	}
	
	@Override
    public void sessionDestroyed(HttpSessionEvent event) throws ClassCastException{
		Object staffObj = event.getSession().getAttribute("userId");
		if (staffObj != null) {
			HashMap<String, Object> hashMap = new HashMap<>();
			hashMap.put("online_status",2);
			try {
                return;
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("session监听 销毁的时候调用"+event.getSession().getAttribute("userId"));
			System.out.println("session监听 销毁调用获取的客户端ip"+event.getSession().getAttribute("ip"));

			System.out.println("进入Session销毁事件,当前在线用户数"+userCount.decrementAndGet());
		}
	}

}