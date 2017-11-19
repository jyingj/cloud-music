package org.music.common.utils;

import java.util.Date;
import java.util.List;

import org.music.common.constants.HttpConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class JsonUtil {
	
	protected static Logger logger = LoggerFactory.getLogger(JsonUtil.class);
	/**
	 * 返回服务端处理结果
	 * 
	 * @param obj
	 *            服务端输出对象
	 * @return 输出处理结果给前段JSON格式数据
	 * @author YANGHONGXIA
	 * @since 2015-01-06
	 */
	public static String responseResult(Object obj) {
		JSONObject jsonObj = null;
		if (obj != null) {
			if(logger.isDebugEnabled()){
				logger.debug("后端返回对象：{}", obj);
			}
			
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
			jsonObj = JSONObject.fromObject(obj, jsonConfig);
			if(logger.isDebugEnabled()){
				logger.debug("后端返回数据：" + jsonObj);
			}
			if (HttpConstants.SERVICE_RESPONSE_SUCCESS_CODE
					.equals(jsonObj.getString(HttpConstants.SERVICE_RESPONSE_RESULT_FLAG))) {
				jsonObj.element(HttpConstants.RESPONSE_RESULT_FLAG_ISERROR, false);
				jsonObj.element(HttpConstants.SERVICE_RESPONSE_RESULT_MSG, "");
				jsonObj.put("resultCode", "1");
				jsonObj.put("resultDesc", "");
			} else {
				jsonObj.element(HttpConstants.RESPONSE_RESULT_FLAG_ISERROR, true);
				String errMsg = jsonObj.getString(HttpConstants.SERVICE_RESPONSE_RESULT_MSG);
				jsonObj.element(HttpConstants.SERVICE_RESPONSE_RESULT_MSG,
						errMsg == null ? HttpConstants.SERVICE_RESPONSE_NULL : errMsg);
				jsonObj.put("resultCode", "0");
				jsonObj.put("resultDesc", errMsg == null ? HttpConstants.SERVICE_RESPONSE_NULL : errMsg);
			}
		}
		if(logger.isDebugEnabled()){
			logger.debug("输出结果：{}", jsonObj.toString());
		}
		return jsonObj.toString();
	}

	/**
	 * 返回成功
	 * 
	 * @param obj
	 *            输出对象
	 * @return 输出成功的JSON格式数据
	 */
	public static String responseSuccess(Object obj) {
		JSONObject jsonObj = null;
		if (obj != null) {
			if(logger.isDebugEnabled()){
				logger.debug("后端返回对象：{}", obj);
			}
/*			 JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Timestamp.class, new JsonDateValueProcessor()); */
			jsonObj = JSONObject.fromObject(obj, JsonConfigUtil.getConfig());
			if(logger.isDebugEnabled()){
				logger.debug("后端返回数据：" + jsonObj);
			}
			
			jsonObj.element(HttpConstants.RESPONSE_RESULT_FLAG_ISERROR, false);
			jsonObj.element(HttpConstants.SERVICE_RESPONSE_RESULT_MSG, "");
			jsonObj.put("resultCode", "1");
			jsonObj.put("resultDesc","");
		}
		if(logger.isDebugEnabled()){
			logger.debug("输出结果：{}", jsonObj.toString());
		}
		
		return jsonObj.toString();
	}

	/**
	 * 返回成功
	 * 
	 * @param obj
	 *            输出对象
	 * @return 输出成功的JSON格式数据
	 */
	public static String responseArraySuccess(Object obj) {
		JSONArray jsonObj = null;
		if (obj != null) {
			if(logger.isDebugEnabled()){
				logger.debug("后端返回对象：{}", obj);
			}
			
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
			jsonObj = JSONArray.fromObject(obj, jsonConfig);
			if(logger.isDebugEnabled()){
				logger.debug("后端返回数据：" + jsonObj);
			}
			
		}
		if(logger.isDebugEnabled()){
			logger.debug("输出结果：{}", jsonObj.toString());
		}
		return jsonObj.toString();
	}

	/**
	 * 返回成功
	 * 
	 * @param obj
	 *            输出对象
	 * @return 输出成功的JSON格式数据
	 */
	public String responseSuccess(Object obj, String msg) {
		JSONObject jsonObj = null;
		if (obj != null) {
			if(logger.isDebugEnabled()){
				logger.debug("后端返回对象：{}", obj);
			}
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
			jsonObj = JSONObject.fromObject(obj, jsonConfig);
			if(logger.isDebugEnabled()){
				logger.debug("后端返回数据：" + jsonObj);
			}
			jsonObj.element(HttpConstants.RESPONSE_RESULT_FLAG_ISERROR, false);
			jsonObj.element(HttpConstants.SERVICE_RESPONSE_RESULT_MSG, msg);
		}
		jsonObj.put("resultCode", "1");
		jsonObj.put("resultDesc", msg);
		if(logger.isDebugEnabled()){
			logger.debug("输出结果：{}", jsonObj.toString());
		}
		return jsonObj.toString();
	}

	/**
	 * 返回失败
	 * 
	 * @param errorMsg
	 *            错误信息
	 * @return 输出失败的JSON格式数据
	 */
	public static String responseFail(String errorMsg) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put(HttpConstants.RESPONSE_RESULT_FLAG_ISERROR, true);
		jsonObj.put(HttpConstants.SERVICE_RESPONSE_RESULT_MSG, errorMsg);
		jsonObj.put("resultCode", "0");
		jsonObj.put("resultDesc", errorMsg);
		if(logger.isDebugEnabled()){
			logger.debug("输出结果：{}", jsonObj.toString());
		}
		return jsonObj.toString();
	}
	
	/**
	 * 返回json格式   {"resultCode":"1","desc":"..."}
	 * @param resultCode
	 * @param desc
	 * @throws Exception
	 */
	public static String out(String resultCode,String resultDesc){
		JSONObject json = new JSONObject();
		json.put("resultCode", resultCode);
		json.put("resultDesc", resultDesc);
		return json.toString();
		
	}
	
	/**
	 * 返回json格式   {"resultCode":"1","data":{...}}
	 * @param resultCode
	 * @param obj
	 * @throws Exception
	 */
	public static String out(String resultCode,Object obj){
		JSONObject json = new JSONObject();
		json.put("resultCode", resultCode);
		if(obj instanceof JSONArray || obj instanceof JSONObject){
			json.put("data", obj);
		} else if (obj instanceof List<?>){
			json.put("data", JSONArray.fromObject(obj));
		} else {
			json.put("data", JSONObject.fromObject(obj));
		}
		return json.toString();
	}

	/**
	 * 返回json格式{"result":true,"desc":"..."}
	 * @param result
	 * @param desc
	 * @return
	 */
	public static String out(boolean result,String desc){
		JSONObject json = new JSONObject();
		json.put("result", result);
		json.put("desc", desc);
		return json.toString();
	}
	
	/**
	 * 返回json格式   {"result":true,"data":{...}} or {"result":true,"data":[{...},{...}]}
	 * @param result
	 * @param obj
	 * @throws Exception
	 */
	public static String out(boolean result,Object obj){
		JSONObject json = new JSONObject();
		json.put("result", result);
		if(obj instanceof JSONArray || obj instanceof JSONObject){
			json.put("data", obj);
		} else if (obj instanceof List<?>){
			json.put("data", JSONArray.fromObject(obj));
		} else {
			json.put("data", JSONObject.fromObject(obj));
		}
		return json.toString();
	}
}
