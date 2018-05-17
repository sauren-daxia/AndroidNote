package com.example.cc.mvvmtest.net

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface API {

  @GET("user/GetShortMessageCode")
  fun login(
      @Query("userMobile") userMobile: String = "",
      @Query("password") password: String = ""
  ): Observable<String>

}