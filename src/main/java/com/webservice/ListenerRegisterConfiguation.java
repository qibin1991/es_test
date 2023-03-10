package com.webservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
//import org.springframework.session.web.http.SessionEventHttpSessionListenerAdapter;

import javax.servlet.http.HttpSessionListener;
import java.util.ArrayList;
import java.util.List;

//@Configuration
//@EnableRedisHttpSession
//public class ListenerRegisterConfiguation {
//
//	@Bean
//    public SessionEventHttpSessionListenerAdapter sessionEventHttpSessionListenerAdapter() {
//		List<HttpSessionListener> httpSessionListeners = new ArrayList<HttpSessionListener>();
//		httpSessionListeners.add(new SessionListener());
//        return new SessionEventHttpSessionListenerAdapter(httpSessionListeners);
//    }
//
//}
