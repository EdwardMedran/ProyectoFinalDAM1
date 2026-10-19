package pe.edu.cibertec.proyectofinaldam1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import pe.edu.cibertec.proyectofinaldam1.databinding.ActivityLoginBinding
import java.io.IOException

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v == binding.btnLogin) {

            val usuario = binding.etUsuario.text.toString().trim()
            val password = binding.etPassword.text.toString()

            if (usuario.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this,
                    "Complete ambos campos",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            iniciarSesion(usuario, password)
        }
    }

    private fun iniciarSesion(usuario: String, password: String) {

        binding.btnLogin.isEnabled = false

        lifecycleScope.launch {
            try {
                val respuesta = RetrofitClient.loginApi.iniciarSesion(
                    LoginRequest(
                        usuario = usuario,
                        password = password
                    )
                )

                if (respuesta.isSuccessful) {

                    val usuarioAutenticado = respuesta.body()

                    if (usuarioAutenticado == null) {
                        Toast.makeText(
                            this@LoginActivity,
                            "El servidor no devolvió datos del usuario",
                            Toast.LENGTH_LONG
                        ).show()
                        return@launch
                    }

                    val sede = usuarioAutenticado.sedeAsignada
                    val sessionManager = SessionManager(this@LoginActivity)

                    sessionManager.guardarSesion(
                        idPersona = usuarioAutenticado.idPersona,
                        nombre = "${usuarioAutenticado.nombre} ${usuarioAutenticado.apellido}",
                        rol = usuarioAutenticado.rol,
                        idSede = sede?.idSede ?: 0,
                        nombreSede = sede?.nombreSede ?: "Sede no asignada",
                        direccion = sede?.direccion ?: "Dirección no disponible",
                        area = usuarioAutenticado.area ?: ""
                    )

                    val intent = Intent(
                        this@LoginActivity,
                        MainActivity::class.java
                    )

                    intent.putExtra(
                        "idPersona",
                        usuarioAutenticado.idPersona
                    )

                    intent.putExtra(
                        "nombre",
                        usuarioAutenticado.nombre
                    )

                    intent.putExtra(
                        "apellido",
                        usuarioAutenticado.apellido
                    )

                    intent.putExtra(
                        "rol",
                        usuarioAutenticado.rol
                    )

                    intent.putExtra(
                        "area",
                        usuarioAutenticado.area
                    )

                    startActivity(intent)
                    finish()

                } else {

                    val mensaje = try {
                        val error = respuesta.errorBody()?.string()

                        if (!error.isNullOrBlank()) {
                            Gson().fromJson(
                                error,
                                ErrorResponse::class.java
                            )?.mensaje
                        } else {
                            null
                        }
                    } catch (_: Exception) {
                        null
                    }

                    Toast.makeText(
                        this@LoginActivity,
                        mensaje ?: when (respuesta.code()) {
                            400 -> "Solicitud inválida"
                            401 -> "Credenciales incorrectas"
                            else -> "Error del servidor: HTTP ${respuesta.code()}"
                        },
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: IOException) {
                Toast.makeText(
                    this@LoginActivity,
                    "No se pudo conectar con el servidor. Verifica la URL y la conexión.",
                    Toast.LENGTH_LONG
                ).show()

            } catch (e: Exception) {
                Toast.makeText(
                    this@LoginActivity,
                    "Ocurrió un error al iniciar sesión.",
                    Toast.LENGTH_LONG
                ).show()

            } finally {
                binding.btnLogin.isEnabled = true
            }
        }
    }
}