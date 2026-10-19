package pe.edu.cibertec.proyectofinaldam1

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val usuario: String,
    val password: String
)

data class LoginResponse(
    @SerializedName("idPersona")
    val idPersona: Int,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("apellido")
    val apellido: String,

    @SerializedName("usuario")
    val usuario: String,

    @SerializedName("rol")
    val rol: String,

    @SerializedName("area")
    val area: String,

    @SerializedName("estado")
    val estado: String
)

data class ErrorResponse(
    val mensaje: String?
)
