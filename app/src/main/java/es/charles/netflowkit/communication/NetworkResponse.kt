package es.charles.netflowkit.communication

import okhttp3.Headers
import java.io.IOException

sealed class NetworkResponse <out T: Any, out U: Any> {
    /**
     * Respuesta ok con body
     */
    data class Success<T : Any>(val body: T, val headers: Headers) : NetworkResponse<T, Nothing>()
    /**
     * Respuesta fallido con mensaje
     */
    data class HttpError<U : Any>(val code: Int, val message: String? ) : NetworkResponse<Nothing, U>()

    /**
     * Network error
     */
    data class NetworkError(val error: IOException, val code: Int?) : NetworkResponse<Nothing, Nothing>()

    /**
     * Por ejemplo, json parsing error
     */
    data class UnknownError(val error: Throwable?) : NetworkResponse<Nothing, Nothing>()

}
