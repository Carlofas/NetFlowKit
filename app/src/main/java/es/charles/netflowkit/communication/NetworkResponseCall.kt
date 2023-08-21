package es.charles.netflowkit.communication

import android.util.Log
import es.charles.netflowkit.communication.BaseRetrofit.mGson
import es.charles.netflowkit.utils.StatusCode
import es.charles.netflowkit.utils.TAG
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.*
import java.io.IOException


internal class NetworkResponseCall<S: Any, E: Any> (
    private val delegate: Call<S>,
    private val errorConverter: Converter<ResponseBody, E>
        ): Call<NetworkResponse<S, E>> {


    override fun enqueue(callback: Callback<NetworkResponse<S, E>>) {

        return delegate.enqueue(object : Callback<S> {
            override fun onResponse(call: Call<S>, response: Response<S>) {
                val body = response.body()
                val code = response.code()
                val error = response.errorBody()
                Log.w("$TAG RESPONSE", "$response\n" +
                        "DATA BODY:\n${mGson.toJson(body)}")

                val headers = response.headers()
                val message = response.headers()["message"]

                if (response.isSuccessful){
                    when (code) {
                         in StatusCode.OK.code..StatusCode.IMUsed.code ->
                             body?.let {
                                     callback.onResponse(
                                     this@NetworkResponseCall,
                                     Response.success(NetworkResponse.Success(it, headers))
                                 )
                            }

                        else ->
                            callback.onResponse(
                                this@NetworkResponseCall,
                                Response.success(NetworkResponse.HttpError(code, message))
                            )
                    }
                } else {
                    callback.onResponse(
                        this@NetworkResponseCall,
                        Response.success(NetworkResponse.HttpError(code,  message))
                    )
                }

            }

            override fun onFailure(call: Call<S>, throwable: Throwable) {
                Log.w("$TAG ONFAILURE",call.request().toString())
                throwable.printStackTrace()
                val networkResponse = when (throwable) {
                    is NoInternetException -> NetworkResponse.NetworkError(
                        error = throwable,
                        code = StatusCode.NoInternetException.code
                    )
                    is IOException ->
                        NetworkResponse.NetworkError(
                            error = throwable,
                            code = StatusCode.Unknown.code
                        )
                    else -> NetworkResponse.UnknownError(throwable)
                }
                callback.onResponse(this@NetworkResponseCall, Response.success(networkResponse))
            }
        })
    }

    override fun isExecuted() = delegate.isExecuted

    override fun clone() = NetworkResponseCall( delegate.clone(), errorConverter)

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<NetworkResponse<S, E>> {
        throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")
    }

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}