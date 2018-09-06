package com.zhang.okinglawenforcementphone.jpush.clien;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2018/8/23/023.
 */

public interface JPushClienService {
    /**
     * 极光推送
     */
    @Headers({"Content-Type:application/json", "Authorization:Basic ZDRjOTIyZWY4NjMxNTk4ODJiMjEzNzhmOjMxNzVhMWYyNzZhZTBmZDg1YjNhNDk0MA=="})
    @POST("v3/push")
    Observable<ResponseBody> pushMessage(@Body RequestBody requestBody);


    @Headers({"Content-Type:application/json", "Authorization:Basic ZDRjOTIyZWY4NjMxNTk4ODJiMjEzNzhmOjMxNzVhMWYyNzZhZTBmZDg1YjNhNDk0MA=="})
    @GET("v3/device/{registration_id}")
    Observable<ResponseBody> findAlias(@Path("registration_id") String registrationId);

    @Headers({"Content-Type:application/json", "Authorization:Basic ZDRjOTIyZWY4NjMxNTk4ODJiMjEzNzhmOjMxNzVhMWYyNzZhZTBmZDg1YjNhNDk0MA=="})
    @POST("v3/device/{registration_id}")
    Observable<ResponseBody> setAlias(@Path("registration_id") String registrationId,@Body RequestBody requestBody);
}
