package com.tjstudy.listendemo.net;

import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.GET;
import rx.Observable;

/**
 * 网络访问接口
 */

public interface NetService {
    @GET("servlet/UploadParam")
    Observable<ResponseBody> uploadParamGetWithField(
            @Field("name") String name
    );
}