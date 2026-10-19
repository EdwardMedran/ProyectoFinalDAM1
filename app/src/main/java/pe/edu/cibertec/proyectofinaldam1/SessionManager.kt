package pe.edu.cibertec.proyectofinaldam1

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences("SIGAPRO_SESSION", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_ID_PERSONA = "idPersona"
        private const val KEY_NOMBRE = "nombre"
        private const val KEY_ROL = "rol"
        private const val KEY_ID_SEDE = "idSede"
        private const val KEY_NOMBRE_SEDE = "nombreSede"
        private const val KEY_DIRECCION = "direccion"
        private const val KEY_SESION_ACTIVA = "sesionActiva"
    }

    fun guardarSesion(
        idPersona: Int,
        nombre: String,
        rol: String,
        idSede: Int,
        nombreSede: String,
        direccion: String
    ) {
        preferences.edit()
            .putInt(KEY_ID_PERSONA, idPersona)
            .putString(KEY_NOMBRE, nombre)
            .putString(KEY_ROL, rol)
            .putInt(KEY_ID_SEDE, idSede)
            .putString(KEY_NOMBRE_SEDE, nombreSede)
            .putString(KEY_DIRECCION, direccion)
            .putBoolean(KEY_SESION_ACTIVA, true)
            .apply()
    }

    fun haySesionActiva(): Boolean {
        return preferences.getBoolean(KEY_SESION_ACTIVA, false)
    }

    fun obtenerIdPersona(): Int {
        return preferences.getInt(KEY_ID_PERSONA, 0)
    }

    fun obtenerNombre(): String {
        return preferences.getString(KEY_NOMBRE, "") ?: ""
    }

    fun obtenerRol(): String {
        return preferences.getString(KEY_ROL, "") ?: ""
    }

    fun obtenerIdSede(): Int {
        return preferences.getInt(KEY_ID_SEDE, 0)
    }

    fun obtenerNombreSede(): String {
        return preferences.getString(KEY_NOMBRE_SEDE, "") ?: ""
    }

    fun obtenerDireccion(): String {
        return preferences.getString(KEY_DIRECCION, "") ?: ""
    }

    fun cerrarSesion() {
        preferences.edit().clear().apply()
    }
}