package pe.edu.cibertec.proyectofinaldam1

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("api/auth/login")
    suspend fun iniciarSesion(
        @Body request: LoginRequest
    ): Response<LoginResponse>
}
