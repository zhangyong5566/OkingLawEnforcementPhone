package com.zhang.baselib.utils;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/4/20.
 */

public class DataUtil {
    /**
     * 判断字符串是否为空 为空即true
     *
     * @param str 字符串
     * @return
     */
    public static boolean isNullString(@Nullable String str) {
        return str == null || str.length() == 0 || "".equals(str) || "null".equals(str);
    }


    public static <T> T praseJson(String jsonData, TypeToken<T> tTypeToken) {
        T object = null;
        Gson g = new GsonBuilder().serializeNulls().registerTypeAdapter(Uri.class, new UristampAdapter()).
                registerTypeAdapter(Long.class, new LongstampAdapter()).create();
        final java.lang.reflect.Type type = tTypeToken.getType();
        try {
            object = g.fromJson(jsonData, type);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return object;
    }

    public static <T> String toJson(T object) {
        Gson g = new GsonBuilder().serializeNulls().registerTypeAdapter(Uri.class, new UristampAdapter()).create();
        return g.toJson(object);
    }

    public static class UristampAdapter implements JsonSerializer<Uri>, JsonDeserializer<Uri> {

        @Override
        public Uri deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (jsonElement == null) {
                return null;
            } else {
                return Uri.fromFile(new File(jsonElement.getAsString()));
            }
        }

        @Override
        public JsonElement serialize(Uri uri, Type type, JsonSerializationContext jsonSerializationContext) {
            String value = "";
            if (uri != null) {
                value = uri.getPath();
            }
            return new JsonPrimitive(value);
        }
    }

    public static class LongstampAdapter implements JsonSerializer<Long>, JsonDeserializer<Long> {

        @Override
        public Long deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            String str = jsonElement.getAsString();
            if ("".equals(str)) {
                return null;
            } else {
                return jsonElement.getAsLong();
            }
        }


        @Override
        public JsonElement serialize(Long aLong, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(aLong);
        }
    }

    public static String getSystemTime() {
        DateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(System.currentTimeMillis());
    }


    /**
     * Created by Administrator on 2018/3/1.
     */

    /**
     * 拆分集合
     *
     * @param <T>
     * @param resList 要拆分的集合
     * @param count   每个集合的元素个数
     * @return 返回拆分后的各个集合
     */
    public static <T> List<List<T>> split(List<T> resList, int count) {

        if (resList == null || count < 1)
            return null;
        List<List<T>> ret = new ArrayList<List<T>>();
        int size = resList.size();
        if (size <= count) { //数据量不足count指定的大小
            ret.add(resList);
        } else {
            int pre = size / count;
            int last = size % count;
            //前面pre个集合，每个大小都是count个元素
            for (int i = 0; i < pre; i++) {
                List<T> itemList = new ArrayList<T>();
                for (int j = 0; j < count; j++) {
                    itemList.add(resList.get(i * count + j));
                }
                ret.add(itemList);
            }
            //last的进行处理
            if (last > 0) {
                List<T> itemList = new ArrayList<T>();
                for (int i = 0; i < last; i++) {
                    itemList.add(resList.get(pre * count + i));
                }
                ret.add(itemList);
            }
        }
        return ret;

    }


}
