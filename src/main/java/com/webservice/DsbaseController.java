
package com.webservice;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;


/**
 * Controller基础类，
 * 
 * @author Alex
 *
 */
public abstract class DsbaseController {
	protected static Log logger = LogFactory.getLog(DsbaseController.class);
	private static String encode = "ISO-8859-1";
	public static String AesSecretKey = "Xybc12#$56&*90@)";

	/**
	 * 编码转换
	 * 
	 * @param str
	 * @return
	 */
	private static String changeEncoding(String str) {
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				str = new String(str.getBytes(encode), "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 判断是否为IE
	 * 
	 * @param request
	 * @return
	 */
	private static boolean isIE(HttpServletRequest request) {
		return request.getHeader("USER-AGENT").toString().toLowerCase().indexOf("msie") > 0;
	}

	/**
	 * 解析REQUEST里FORM数据，生成MAP
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	protected static Map<String, Object> getRequestMap(HttpServletRequest request) throws Exception {
		request.setCharacterEncoding("UTF-8");
		// 参数Map
		Map<String, String[]> properties = request.getParameterMap();
		// 返回值Map
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Iterator entries = properties.entrySet().iterator();
		Map.Entry entry;
		String name = "";
		while (entries.hasNext()) {
			String value = "";
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if (null == valueObj) {
				value = "";
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value += values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			returnMap.put(name, changeEncoding(value));
		}

//		returnMap.put("op_date", new Date().getTime()/1000);
//		returnMap.put("op_staff_id", request.getSession().getAttribute("op_staff_id"));
//		returnMap.put("op_staff_name", request.getSession().getAttribute("op_staff_name"));
//		returnMap.put("op_login_name", request.getSession().getAttribute("op_login_name"));
//		returnMap.put("op_tenant_id", request.getSession().getAttribute("op_tenant_id"));
//		returnMap.put("op_tenant_name", request.getSession().getAttribute("op_tenant_name"));
//		returnMap.put("op_phone", request.getSession().getAttribute("op_phone"));
//		returnMap.put("op_roles", request.getSession().getAttribute("op_roles"));
//		returnMap.put("op_hotel_id", request.getSession().getAttribute("op_hotel_id"));
//		returnMap.put("op_hotel_abbr", request.getSession().getAttribute("op_hotel_abbr"));
//		returnMap.put("op_overbook", request.getSession().getAttribute("op_overbook"));
//		returnMap.put("op_half_account_time", request.getSession().getAttribute("op_half_account_time"));
//		returnMap.put("op_hour_room_hours", request.getSession().getAttribute("op_hour_room_hours"));
		
		return returnMap;
	}

	/**
	 * HTTP 返回输出成功的数据实体。
	 * 
	 * @param request
	 * @param response
	 * @param responseBean
	 */
	protected static void writeResult(HttpServletRequest request, HttpServletResponse response,
			ResponseBean responseBean) {
		writeJsonResult(request, response, JSONObject.toJSONString(responseBean, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect));
	}

	/**
	 * HTTP 返回输出成功的数据实体。
	 *
	 * @param request
	 * @param response
	 * @param i6000Result
	 */
	protected static void writeResult(HttpServletRequest request, HttpServletResponse response,
									  I6000Result i6000Result) {
		writeJsonResult(request, response, JSONObject.toJSONString(i6000Result, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect));
	}

	/**
	 * HTTP 返回输出成功的数据实体。
	 * @param i6000Result
	 */
	protected static String writeResult(String i6000Result) {
		return JSONObject.toJSONString(i6000Result, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect);
	}


	/**
	 * HTTP 返回输出成功的数据实体，输出有UUID。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	protected static boolean writeResult(HttpServletRequest request, HttpServletResponse response, int code,
			String msg, Object body) {
		ResponseBean responseBean = new ResponseBean();

		responseBean.setBody(body);
		return writeJsonResult(request, response, JSONObject.toJSONString(responseBean, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect));
	}

	protected void writeResult(HttpServletRequest request, HttpServletResponse response, int code, String msg) {
		writeResult(request, response, code, msg, null);
	}

	/**
	 * HTTP 输出一个指定的json串。
	 * 
	 * @param request
	 * @param response
	 * @param json
	 * @return
	 */
	protected static boolean writeJsonResult(HttpServletRequest request, HttpServletResponse response, String json) {
		try {
//			response.addHeader("Access-Control-Allow-Origin", "*");
			response.setCharacterEncoding("UTF-8");
			if (isIE(request)) {
				response.setContentType("text/html;charset=UTF-8");

			} else {
				response.setContentType("application/json;charset=UTF-8");
			}
			response.getWriter().print(json);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * HTTP 输出一个指定的json串，用于 DataGrid的分页信息,只输出body数据。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	protected static boolean writeBodyResult(HttpServletRequest request, HttpServletResponse response,
			ResponseBean responseBean) {
		return writeJsonResult(request, response, JSONObject.toJSONString(responseBean.body, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect));
	}

	protected static boolean writeBodyResult(HttpServletRequest request, HttpServletResponse response, String json) {
		ResponseBean responseBean = JSONObject.parseObject(json, ResponseBean.class);
		return writeJsonResult(request, response, JSONObject.toJSONString(responseBean.body, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect));
	}
	
	protected void writeSuccessResult(HttpServletRequest request, HttpServletResponse response, Object body) {
		ResponseBean responseBean = getResponseBean(0, "SUCCESS", body);
		writeResult(request, response, responseBean);
	}

	protected void writeSuccessResultI6000(HttpServletRequest request, HttpServletResponse response, Map<String, Object> responseMap) {
		I6000Result i6000Result = getI6000Result(0, (String) responseMap.get("status"), responseMap);
		writeResult(request, response, i6000Result);
	}

	protected String writeSuccessResultI6000WebService(Map<String, Object> responseMap) {
		I6000Result i6000Result = getI6000Result(0, (String) responseMap.get("status"), responseMap);
		return writeResult(i6000Result.toString());
	}
	
	protected void writeFailureResult(HttpServletRequest request, HttpServletResponse response, String msg) {
		ResponseBean responseBean = getResponseBean(-1, msg , null);
		writeResult(request, response, responseBean);
	}
	
	protected ResponseBean getResponseBean(int code, String msg, Object body) {
		ResponseBean rb = new ResponseBean();

		rb.setBody(body);
		return rb;
	}

	protected I6000Result getI6000Result(int code, String msg, Map<String, Object> responseMap) {
		I6000Result i6000Result = new I6000Result();
		i6000Result.setStatus((String) responseMap.get("status"));
		i6000Result.setMessage((String) responseMap.get("message"));
		i6000Result.setReason((String) responseMap.get("reason"));
		i6000Result.setKpis((Object[]) responseMap.get("kpis"));
		return i6000Result;
	}
	
	public static Map<String, Object> getRequestParameter(HttpServletRequest request) throws Exception {
		Map<String, Object> map = getRequestMap(request);
//		return new ResultMap<String, Object>(map);
		return map;
	}
	
	/**
	 * @author yeTao
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, Object> getRequestMapExceptNull(HttpServletRequest request) throws Exception {
		request.setCharacterEncoding("UTF-8");
		// 参数Map
		Map<String, String[]> properties = request.getParameterMap();
		// 返回值Map
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Iterator entries = properties.entrySet().iterator();
		Map.Entry entry;
		String name = "";
		String value = "";
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if (null == valueObj) {
				value = null;
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			if (value == null || value.trim().length() == 0) {
				continue;
			}
			returnMap.put(name, changeEncoding(value));
		}
		return returnMap;
	}

}
