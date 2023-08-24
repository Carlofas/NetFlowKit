package es.charles.netflowkitdemo.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.charles.netflowkit.communication.BaseRetrofit
import es.charles.netflowkit.communication.ConnectivityInterceptor
import es.charles.netflowkitdemo.remote.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetFlowKitModule {

    @Singleton
    @Provides
    fun provideNetFlowKitInstance(context : Context) : Retrofit {
        val retrofit = BaseRetrofit.getInstance(
            baseUrl = "https://63b54fad0f49ecf508a0abcb.mockapi.io/mock/",
            client =
            OkHttpClient().newBuilder().apply {
                interceptors().add(ConnectivityInterceptor(context))
                readTimeout(1, TimeUnit.MINUTES)
                writeTimeout(1, TimeUnit.MINUTES)
                connectTimeout(1, TimeUnit.MINUTES)
            }.build()
        )
        return retrofit
    }

    @Singleton
    @Provides
    fun provideOperacionApiClient(retrofit: Retrofit) : ApiService {
        return retrofit.create(ApiService::class.java)
    }
}