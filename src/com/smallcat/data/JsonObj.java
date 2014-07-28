package com.smallcat.data;

import java.util.Iterator;

import org.json.JSONObject;
import org.json.JSONArray;

public class JsonObj {
	
	public JsonObj(byte[] bytes) {
		loadString(new String(bytes));
	}
	
	public JsonObj(String string) {
		loadString(string);
	}
	
	private void loadString(String string) {
		jsonStr = string;
		try {
			map = new JSONObject(jsonStr);
		}catch (Exception e) {
			try {
				arr = new JSONArray(jsonStr);
			}catch (Exception e1) {
				// Nothing...
			}
		}
	}
	
	
	public int count() {
		if (arr == null && map == null) {
<<<<<<< HEAD
			return -1;
=======
			return 0;
>>>>>>> ui-lhc
		}else {
			if (arr != null)
				return arr.length();
			else
				return map.length();
		}
	}
	
	public String[] keys() {
		try {
			String[] keys = new String[map.length()];
			@SuppressWarnings("rawtypes")
			Iterator iter = map.keys();
			int index = 0;
			while (iter.hasNext()) {
				String key = (String) iter.next();
				keys[index++] = key;
			}
			return keys;
		}catch (Exception e) {
			return new String[0];
		}
	}
	
	public int[] range() {
		try {
			int[] range = new int[arr.length()];
			for (int i=0; i<range.length; i++) {
				range[i] = i;
			}
			return range;
		}catch (Exception e) {
			return new int[0];
		}
	}
	
<<<<<<< HEAD
	
=======
	public JsonObj[] values() {
		JsonObj[] values = new JsonObj[count()];
		if (arr != null) {
			int count = count();
			for (int i=0; i<count; i++) {
				values[i] = this.get(i);
			}
		}else if (map != null){
			int i = 0;
			for (String key : keys()) {
				values[i++] = this.get(key);
			}
		}
		return values;
	}
>>>>>>> ui-lhc
	
	
	public JsonObj get(int index) {
		try{
			return new JsonObj(arr.get(index).toString());
		}catch (Exception e) {
			return null;
		}
	}
	
	public JsonObj get(String key) {
		try {
			return new JsonObj(map.get(key).toString());
		}catch (Exception e) {
			return null;
		}
	}
	
	
	// arr get
	public Integer getInt(int index) {
		try{
			return arr.getInt(index);
		}catch (Exception e) {
			return null;
		}
	}
	
	public Double getDouble(int index) {
		try {
			return arr.getDouble(index);
		}catch (Exception e) {
			return null;
		}
	}
	
	public Boolean getBool(int index) {
		try {
			return arr.getBoolean(index);
		}catch (Exception e) {
			return null;
		}
	}
	
	public String getString(int index) {
		try {
			return arr.getString(index);
		}catch (Exception e) {
			return null;
		}
	}
	
	
	// map get
	public Integer getInt(String key) {
		try {
			return map.getInt(key);
		}catch (Exception e) {
			return null;
		}
	}
	
	public Double getDouble(String key) {
		try {
			return map.getDouble(key);
		}catch (Exception e) {
			return null;
		}
	}
	
	public Boolean getBool(String key) {
		try {
			return map.getBoolean(key);
		}catch (Exception e) {
			return null;
		}
	}
	
	public String getString(String key) {
		try {
			return map.getString(key);
		}catch (Exception e) {
			return null;
		}
	}
	
	
	
	public String toString() {
		//if (map == null && arr == null) {
		//	return jsonStr;
		//}
		return jsonStr;
	}
	
	public Integer toInt() {
		if (map == null && arr == null) {
			try {
				return Integer.parseInt(jsonStr);
			}catch (Exception e) {
				return null;
			}
		}
		return null;
	}
	
	public Double toDouble() {
		if (map == null && arr == null) {
			try {
				return Double.parseDouble(jsonStr);
			}catch (Exception e) {
				return null;
			}
		}
		return null;
	}
	
	public Boolean toBool() {
		if (map == null && arr == null) {
			try {
				return Boolean.parseBoolean(jsonStr);
			}catch (Exception e) {
				return null;
			}
		}
		return null;
	}
	

	
	//public boolean isString() {
	//	return toString() != null;
	//}
	
	public boolean isInt() {
		return toInt() != null;
	}
	
	public boolean isDouble() {
		return toDouble() != null;
	}
	
	public boolean isBool() {
		return toBool() != null;
	}
	
	
	
	private String jsonStr = null;
	private JSONObject map = null;
	private JSONArray arr = null;
}
