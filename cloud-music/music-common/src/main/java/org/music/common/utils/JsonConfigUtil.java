package org.music.common.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class JsonConfigUtil {

	public static JsonConfig getConfig() {
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonValueProcessor() {
			private final String format = "yyyy-MM-dd HH:mm:ss";

			public Object processObjectValue(String key, Object value, JsonConfig arg2) {
				if (value == null)
					return "";
				if (value instanceof java.util.Date) {
					return new SimpleDateFormat(format).format((java.util.Date) value);
				}
				return value.toString();
			}

			public Object processArrayValue(Object value, JsonConfig arg1) {
				return null;
			}

		});
		jsonConfig.registerJsonValueProcessor(java.sql.Date.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig arg2) {
				if (value == null)
					return "";
				if (value instanceof java.sql.Date) {
					String valStr = "";
					try{
						valStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((java.sql.Date) value);
						if(valStr.endsWith(" 00:00:00")){
							valStr = valStr.replace(" 00:00:00", "");
						}
					} catch (Exception e){
						valStr = new SimpleDateFormat("yyyy-MM-dd").format((java.sql.Date) value);
					}
					return valStr;
				}
				return value.toString();
			}
			
			public Object processArrayValue(Object value, JsonConfig arg1) {
				return null;
			}
			
		});
		jsonConfig.registerJsonValueProcessor(Timestamp.class, new JsonValueProcessor() {
			private final String format = "yyyy-MM-dd HH:mm:ss";
			
			public Object processObjectValue(String key, Object value, JsonConfig arg2) {
				if (value == null)
					return "";
				if (value instanceof java.util.Date) {
					return new SimpleDateFormat(format).format((java.util.Date) value);
				}
				return value.toString();
			}
			
			public Object processArrayValue(Object value, JsonConfig arg1) {
				return null;
			}
			
		});
		jsonConfig.registerJsonValueProcessor(java.lang.String.class, new JsonValueProcessor() {
			private final String format = "";
			
			public Object processObjectValue(String key, Object value, JsonConfig arg2) {
				if (value == null)
					return "";
				return value.toString();
			}
			
			public Object processArrayValue(Object value, JsonConfig arg1) {
				return null;
			}
			
		});
		 
		return jsonConfig;
	} 
 
  

}
