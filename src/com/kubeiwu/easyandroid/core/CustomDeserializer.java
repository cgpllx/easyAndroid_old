package com.kubeiwu.easyandroid.core;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.kubeiwu.easyandroid.text.JsonResult;

public class CustomDeserializer implements JsonDeserializer<JsonResult<?>> {

	public static final Gson gson = new Gson();

	public JsonResult<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		if (json == null)
			return null;
		else {
			JsonElement jecode = json.getAsJsonObject().get("code");
			JsonElement jedesc = json.getAsJsonObject().get("desc");

			if (jecode != null && jecode.isJsonPrimitive() && jecode.getAsString() instanceof String) {
				String code = jecode.getAsString();
				String desc = jedesc.getAsString();
				System.out.println("desc=" + desc);
				System.out.println("code=" + code);
				if ("C0000".equals(code)) {
					return gson.fromJson(json, typeOfT);
				} else {
					JsonResult<?> jsonResult = new JsonResult<>();
					jsonResult.setCode(code);
					jsonResult.setDesc(desc);
					return jsonResult;
				}
			}
			return null;
		}
	}
}