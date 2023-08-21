package es.charles.netflowkit.communication

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BaseRetrofit {

    private var retrofit: Retrofit? = null
    val mGson: Gson =
        GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .serializeNulls()
            .setPrettyPrinting()
            .create()
    fun getInstance(
        baseUrl: String,
        client: OkHttpClient? = null,
        gson: Gson = mGson
    ) : Retrofit {
        val builder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))

        return retrofit ?: kotlin.run {
            retrofit = client?.let {
                builder.client(it).build()
            } ?: kotlin.run {
                builder.build()
            }
            retrofit!!
        }
    }
}