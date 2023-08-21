package es.charles.netflowkit.communication

import java.io.IOException

class NoInternetException : IOException() {

    override val message: String
        get() = "Parece que no hay conexión a internet disponible en este momento. Por favor, revisa tu conexión y vuelve a intentarlo más tarde."
}