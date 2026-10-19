package pe.edu.cibertec.proyectofinaldam1

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AsistenciaApi {
    @POST("api/asistencias")
    suspend fun registrarAsistencia(
        @Body request: AsistenciaRequest
    ): Response<AsistenciaResponse>
}

