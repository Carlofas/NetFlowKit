package es.charles.netflowkit.utils

import es.charles.netflowkit.communication.NetworkResponse
import okhttp3.Headers

const val TAG = "NetFlowKit"
typealias GenericResponse<S> = NetworkResponse<S, Error>

suspend fun <T : Any> NetworkResponse<T, Error>.onSuccess(
    executable: suspend (T) -> Unit
): NetworkResponse<T, Error> = apply {
    if (this is NetworkResponse.Success)
        executable(body)
}

suspend fun <T : Any> NetworkResponse<T, Error>.onSuccessWithHeaders(
    executable: suspend (T, Headers) -> Unit
): NetworkResponse<T, Error> = apply {
    if (this is NetworkResponse.Success)
        executable(body, headers)
}


suspend fun <T : Any> NetworkResponse<T, Error>.onError(
    executable: suspend (code: Int, message: String?) -> Unit
): NetworkResponse<T, Error> = apply {
    if (this is NetworkResponse.NetworkError)
        executable(code?: StatusCode.Unknown.code, error.message)
    if (this is NetworkResponse.HttpError)
        executable(code, message)
}

suspend fun <T : Any> NetworkResponse<T, Error>.onException(
    executable: suspend (e: Throwable) -> Unit
): NetworkResponse<T, Error> = apply {
    if (this is NetworkResponse.UnknownError)
        executable(error!!)
}
