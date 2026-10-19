package pe.edu.cibertec.proyectofinaldam1

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PerfilActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        val sessionManager = SessionManager(this)

        val txtNombrePerfil = findViewById<TextView>(R.id.txtNombrePerfil)
        val txtRolPerfil = findViewById<TextView>(R.id.txtRolPerfil)
        val txtAreaPerfil = findViewById<TextView>(R.id.txtAreaPerfil)
        val txtIdSede = findViewById<TextView>(R.id.txtIdSede)
        val txtNombreSede = findViewById<TextView>(R.id.txtNombreSede)
        val txtDireccionSede = findViewById<TextView>(R.id.txtDireccionSede)

        txtNombrePerfil.text = sessionManager.obtenerNombre()
        txtRolPerfil.text = sessionManager.obtenerRol()
        txtIdSede.text = sessionManager.obtenerIdSede().toString()
        txtNombreSede.text = sessionManager.obtenerNombreSede()
        txtDireccionSede.text = sessionManager.obtenerDireccion()
        txtAreaPerfil.text = intent.getStringExtra("area") ?: "Área no disponible"
    }
}