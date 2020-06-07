package com.test.myapplication.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.test.myapplication.util.ImageModel
import com.test.myapplication.util.MediaResourceModel
import com.test.myapplication.util.PhotoResponse
import com.test.myapplication.util.WebAntApiService
import io.reactivex.Observable
import io.reactivex.Single
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class PhotoRepository {
    fun loadImages(page: Int, limit: Int, new: Boolean, popular: Boolean): Observable<PhotoResponse> {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://gallery.dev.webant.ru/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(WebAntApiService::class.java).loadImages(page, limit, new, popular)
    }

    fun loadPhotoResource(imageModels: List<ImageModel>): Single<List<ImageModel>>? {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://gallery.dev.webant.ru/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return Single.create { subscriber ->
            val realm = Realm.getDefaultInstance()
            for (model in imageModels) {
                val results = realm.where<MediaResourceModel>().equalTo("id", model.id).findAll()
                if (results.isEmpty()) {
                    val call =
                        retrofit.create(WebAntApiService::class.java).loadMediaResource(model.id)
                    val response = call.execute()

                    realm.executeTransaction{
                        val obj = realm.createObject<MediaResourceModel>()
                        obj.file = response.body()?.file.toString()
                        obj.id = response.body()?.id!!
                    }

                    model.base64 = response.body()?.file.toString()
                } else {
                    model.base64 = results[0]?.file.toString()
                }

                val encodedImage = model.base64.split(",")[1]
                val decodedString: ByteArray = Base64.decode(encodedImage, Base64.DEFAULT)
                val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                var width: Int = decodedByte.width
                var height: Int = decodedByte.height
                while (width > 1000 || height > 1000) {
                    width /= 2
                    height /= 2
                }
                model.bitmap = Bitmap.createScaledBitmap(decodedByte, width, height, false)
            }
            subscriber.onSuccess(imageModels)
        }
    }

}