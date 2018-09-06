package com.zhang.okinglawenforcementphone.http.service;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2018/2/27.
 */

public interface GDWaterService {
    /**
     * 检测更新
     * /cs/cscx
     */
    @FormUrlEncoded
    @POST("gdWater/cs/cscx")
    Observable<ResponseBody> reqAppVersion(@Field("lx") String lx);

    /**
     * 登录
     * params.account
     * params.password
     */
    @FormUrlEncoded
    @POST("gdWater/login/loginByPad")
    Observable<ResponseBody> login(@Field("account") String account, @Field("password") String password);

    /**
     * 请求权限菜单menu
     * /menu/getMenuTreeForAndroid
     */
    @GET("gdWater/menu/getMenuTreeForAndroid")
    Observable<ResponseBody> loadMenu();

    /**
     * 获取巡查任务
     * taskPublish/getTaskPublishDatas
     */
    @FormUrlEncoded
    @POST("gdWater/taskPublish/getTaskPublishReceiverListDatasForAndroid")
    Observable<ResponseBody> loadHttpMission(@Field("receiver") String receiver, @Field("classify") int classify, @Field("nonsort") String nonsort);

    /**
     * 获取成员
     * taskPerson/getTaskPersonDatas
     */
    @FormUrlEncoded
    @POST("gdWater/taskPerson/getTaskPersonDatas")
    Observable<ResponseBody> loadMember(@Field("task_id") String taskId);

    /**
     * 获取能选择队员（任务安排）
     */
    @FormUrlEncoded
    @POST("gdWater/cs/getUserByPosition")
    Observable<ResponseBody> loadCanSelectMember(@Field("lx") String lx);

    /**
     * 添加队员
     * app/addwxuser
     */
    @FormUrlEncoded
    @POST("gdWater/app/addwxuser")
    Observable<ResponseBody> addMember(@Field("userid") String userid, @Field("task_id") String task_id, @Field("userids") String userids);

    /**
     * 确认选择队员
     * taskPublish/rwfb
     */
    @FormUrlEncoded
    @POST("gdWater/taskPublish/rwfb")
    Observable<ResponseBody> addQRMission(@Field("lx") String lx, @Field("id") String id, @Field("jsrid") String jsrid);


    /**
     * 改变任务状态
     * taskPublish/updatePublishStatus
     */
    @FormUrlEncoded
    @POST("gdWater/taskPublish/updatePublishStatus")
    Observable<ResponseBody> updateMissionState(@Field("id") String id, @Field("execute_start_time") String execute_start_time, @Field("execute_end_time") String execute_end_time, @Field("status") int status);


    /**
     * 获取任务日志
     * taskLog/getTaskLogDatas
     */
    @FormUrlEncoded
    @POST("gdWater/taskLog/getTaskLogDatas")
    Observable<ResponseBody> getHttpMissionLog(@Field("nonsort") String nonsort, @Field("task_id") String task_id);

    /**
     * 获取日志图片路径
     * taskLog/mobile/getImagePath
     */
    @FormUrlEncoded
    @POST("gdWater/taskLog/mobile/getImagePath")
    Observable<ResponseBody> getMissionRecordPicPath(@Field("log_id") String log_id, @Field("type") int type);

    /**
     * 下载文件
     *
     * @param fileUrl
     * @return
     */

    @Streaming //大文件时要加不然会OOM
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);

    /**
     * 提交文本资料
     * taskLog/addAndupdateTaskLogDatas
     */
    @FormUrlEncoded
    @POST("gdWater/taskLog/addAndupdateTaskLogDatas")
    Observable<ResponseBody> uploadJobLogForText(@FieldMap Map<String, Object> fields);

    /**
     * 上传文件
     */
    @Multipart
    @POST("gdWater/taskLog/mobile/uploadfile")
    Observable<ResponseBody> uploadFiles(@PartMap Map<String, RequestBody> params);

    /**
     * 改变状态
     * taskLog/updateTaskLogStatus
     */
    @FormUrlEncoded
    @POST("gdWater/taskLog/updateTaskLogStatus")
    Observable<ResponseBody> updateMissionRecordState(@Field("id") String id, @Field("status") int status);

    /**
     * 上传地理位置
     * publicController/addUserSite
     */
    @FormUrlEncoded
    @POST("gdWater/publicController/addUserSite")
    Observable<ResponseBody> uploadLocation(@Field("LOGINTIME") Long logintime, @Field("USER_ID") String userId, @Field("DEV_ID") String devicId, @Field("COORDINATE") String coordinate, @Field("TIME") String time);

    /**
     * 获取紧急任务列表
     * /taskPublish/getAllEmergencyTaskForAndroid
     */
    @FormUrlEncoded
    @POST("gdWater/taskPublish/getAllEmergencyTaskForAndroid")
    Observable<ResponseBody> loadTaskList(@Field("USERID") String userId, @Field("deptId") String deptId, @Field("fbrid") String fbrid);

    /**
     * 删除紧急任务
     * /taskPublish/deleteEmergencyReleaseForAndroid
     */
    @FormUrlEncoded
    @POST("gdWater/taskPublish/deleteEmergencyReleaseForAndroid")
    Observable<ResponseBody> deleteEmergencyRelease(@Field("id") String id);

    /**
     * 获取任务名称
     * /taskPublish/getMcForAndroid
     */
    @FormUrlEncoded
    @POST("gdWater/taskPublish/getMcForAndroid")
    Observable<ResponseBody> loadEmergencyName(@Field("deptId") String deptId);

    /**
     * 新增紧急任务
     * /taskPublish/insertEmergencyReleaseForAndroid
     */
    @FormUrlEncoded
    @POST("gdWater/taskPublish/insertEmergencyReleaseForAndroid")
    Observable<ResponseBody> addEmergencyRelease(@FieldMap Map<String, Object> fields);

    /**
     * 获取人员
     * /cs/getUserByPosition
     */
    @FormUrlEncoded
    @POST("gdWater/cs/getUserByPosition")
    Observable<ResponseBody> loadPersonnel(@Field("lx") String lx);

    /**
     * 获取天气
     * v3/weather/now.json
     */
    @GET("/v3/weather/now.json")
    Observable<ResponseBody> loadTemp(@Query("key") String key, @Query("location") String location, @Query("language") String language, @Query("unit") String unit);

    /**
     * 获取巡查任务
     */
    @FormUrlEncoded
    @POST("gdWater/taskPublish/rwfb")
    Observable<ResponseBody> loadPatrolls(@Field("lx") String lx, @Field("USERID") String userid, @Field("deptId") String deptId, @Field("fbrid") String fbrid);

    /**
     * 撤回任务
     * /taskPublish/withDraw
     */
    @FormUrlEncoded
    @POST("gdWater/taskPublish/withDraw")
    Observable<ResponseBody> withDraw(@Field("id") String id, @Field("sprid") String sprid, @Field("fbrid") String fbrid);

    /**
     * 删除任务
     * /taskPublish/contractorDeleteForAndroid
     */
    @FormUrlEncoded
    @POST("gdWater/taskPublish/contractorDeleteForAndroid")
    Observable<ResponseBody> deleteTask(@Field("id") String id);


    /**
     * 更新一般巡查任务
     * /taskPublish/contractorUpdateForAndroid
     */

    @FormUrlEncoded
    @POST("gdWater/taskPublish/contractorUpdateForAndroid")
    Observable<ResponseBody> updateOrdinaryTask(@FieldMap Map<String, Object> fields);

    /**
     * 更新一般巡查任务
     * /taskPublish/contractorInsertForAndroid
     */

    @FormUrlEncoded
    @POST("gdWater/taskPublish/contractorInsertForAndroid")
    Observable<ResponseBody> pushOrdinaryTask(@FieldMap Map<String, Object> fields);


    /**
     * 获取装备
     * /equiPmentController/getEquipmentsByDeptIdForAndroid
     */

    @GET("gdWater/equiPmentController/getEquipmentsByDeptIdForAndroid")
    Observable<ResponseBody> loadEquipment();


    /**
     * /equiPmentController/getEquipmentsByDeptIdForAndroid
     */

    @GET("gdWater/taskLog/getLogPageForAndroid")
    Observable<ResponseBody> loadPatrolLogManagement(@Query("limit") int limit, @Query("offset") int offset);

    /**
     * 获取案件
     * app/caseAll
     */
    @FormUrlEncoded
    @POST("gdWater/app/caseAll")
    Observable<ResponseBody> loadCaseList(@Field("uid") String uid);

    /**
     * 上传证据
     * app/caseEvidenceSave
     */
    @FormUrlEncoded
    @POST("gdWater/app/caseEvidenceSave")
    Observable<ResponseBody> uploadEvidence(@FieldMap Map<String, Object> fields);


    /**
     * 上传文件
     */
    @Multipart
    @POST("gdWater/app/uploadfile")
    Observable<ResponseBody> uploadEvidenceFiles(@PartMap Map<String, RequestBody> params);


    /**
     * 获取联系人
     * /equiPmentController/getEquipmentsByDeptIdForAndroid
     */

    @GET("gdWater/user/getUserByDeptIdForAndroid")
    Observable<ResponseBody> loadDepatContacts(@Query("limit") int limit, @Query("offset") int offset);


    /**
     * 上传笔录
     * /dcblxx/addDcblForAN
     */
    @FormUrlEncoded
    @POST("gdWater/dcblxx/addDcblForAN")
    Observable<ResponseBody> uploadRecord(@FieldMap Map<String, Object> params);

    /**
     * 上传预立案
     * /yla/addYlaForAndroid
     */
    @FormUrlEncoded
    @POST("gdWater/yla/addYlaForAndroid")
    Observable<ResponseBody> uploadCaseInAdvance(@FieldMap Map<String, Object> params);

    /**
     * 更改信息
     * /publicController/updUsers
     */
    @FormUrlEncoded
    @POST("gdWater/publicController/updUsers")
    Observable<ResponseBody> uploadUserInfo(@FieldMap Map<String, Object> params);

    /**
     * 任务审核
     * /publicController/updUsers
     */
    @FormUrlEncoded
    @POST("gdWater/taskForAndroid/taskPublishSpForAndroid")
    Observable<ResponseBody> taskReview(@FieldMap Map<String, Object> params);

    /**
     * 任务退回
     */
    @FormUrlEncoded
    @POST("gdWater/taskForAndroid/taskPublishSpForAndroid")
    Observable<ResponseBody> taskBack(@FieldMap Map<String, Object> params);

    /**
     * 获取一条基本日志信息
     */
    @FormUrlEncoded
    @POST("gdWater/taskLog/getTaskLogDataByTaskid")
    Observable<ResponseBody> getBasicLog(@Field("taskid") String taskId);

    /**
     * 获取日志图片
     */

    @FormUrlEncoded
    @POST("gdWater/taskLog/getImgByLogId")
    Observable<ResponseBody> loadTaskPic(@Field("LOG_ID") String logId);

    /**
     * 查询同部门的人员信息
     */

    @FormUrlEncoded
    @POST("gdWater/user/getUsersByDeptIdForAndroid")
    Observable<ResponseBody> getUsersByDeptId(@Field("deptId") String deptId);

    /**
     * 获取受理编号
     */


    @GET("gdWater/taskForAndroid/getSlidByParameterForAndroid")
    Observable<ResponseBody> loadAcceptNumber();


    /**
     * 获取单位
     */


    @FormUrlEncoded
    @POST("gdWater/cs/getSameLevelDeptForAndroid")
    Observable<ResponseBody> loadUnit(@Field("deptId") String deptId);

    /**
     * 提交批示(已阅)
     */
    @FormUrlEncoded
    @POST("gdWater/taskForAndroid/examineOthersLogForAndroid")
    Observable<ResponseBody> examineOthersLogForAndroid(@FieldMap Map<String, Object> params);

    /**
     * 提交批示(立案处罚)
     */
    @FormUrlEncoded
    @POST("gdWater/taskForAndroid/examineLogForAndroid")
    Observable<ResponseBody> examineLogForAndroid(@FieldMap Map<String, Object> params);

    /**
     * 案由
     */
    @FormUrlEncoded
    @POST("gdWater/taskForAndroid/getTaskAyByParameterForAndroid")
    Observable<ResponseBody> getTaskAyByParameterForAndroid(@Field("taskLogId") String taskLogId);

    /**
     * 案由
     */
    @FormUrlEncoded
    @POST("gdWater/rwdb/rwdba")
    Observable<ResponseBody> rwdba(@Field("lx") String lx, @Field("dbrwid") String dbrwid, @Field("rwlx") String rwlx, @Field("jsr") String jsr);





}


