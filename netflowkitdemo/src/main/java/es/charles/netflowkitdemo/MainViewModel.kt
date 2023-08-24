package es.charles.netflowkitdemo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.charles.netflowkit.utils.onError
import es.charles.netflowkit.utils.onException
import es.charles.netflowkit.utils.onSuccess
import es.charles.netflowkitdemo.remote.ApiService
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val apiService: ApiService
): ViewModel() {


    fun mockCall() = viewModelScope.launch {
        apiService.getMockData()
            .onSuccess { response ->
                Log.d("MOCK CALL SUCCESS", "DATA -> \n $response")
            }
            .onError { code, message ->
               Log.d("MOCK CALL ERROR", "Code -> $code\nMessage -> $message")
            }
            .onException { ex ->
                Log.d("MOCK CALL EX", "Exception -> ${ex.message}")
                ex.printStackTrace()
            }
    }

}