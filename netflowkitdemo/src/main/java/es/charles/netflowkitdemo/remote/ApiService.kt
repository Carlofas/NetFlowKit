package es.charles.netflowkitdemo.remote

import es.charles.netflowkit.utils.GenericResponse
import es.charles.netflowkitdemo.remote.models.MockModel
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {

    @GET("pruebaaaaa")
    suspend fun getMockData (): GenericResponse<List<MockModel>>

}