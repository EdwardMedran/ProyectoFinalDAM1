package pe.edu.cibertec.proyectofinaldam1


import android.content.Intent
import androidx.appcompat.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class PerfilFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.fragment_perfil,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val sessionManager = SessionManager(requireContext())

        val txtNombrePerfil =
            view.findViewById<TextView>(R.id.txtNombrePerfil)

        val txtRolPerfil =
            view.findViewById<TextView>(R.id.txtRolPerfil)

        val txtAreaPerfil =
            view.findViewById<TextView>(R.id.txtAreaPerfil)

        val txtIdSede =
            view.findViewById<TextView>(R.id.txtIdSede)

        val txtNombreSede =
            view.findViewById<TextView>(R.id.txtNombreSede)

        val txtDireccionSede =
            view.findViewById<TextView>(R.id.txtDireccionSede)

        txtNombrePerfil.text =
            sessionManager.obtenerNombre().ifBlank {
                "Nombre no disponible"
            }

        txtRolPerfil.text =
            sessionManager.obtenerRol().ifBlank {
                "Rol no disponible"
            }

        val area = sessionManager.obtenerArea()

        txtAreaPerfil.text =
            area.takeIf { it.isNotBlank() }
                ?: "Área no disponible"

        val idSede = sessionManager.obtenerIdSede()

        txtIdSede.text =
            if (idSede > 0) {
                idSede.toString()
            } else {
                "No disponible"
            }

        txtNombreSede.text =
            sessionManager.obtenerNombreSede().ifBlank {
                "Sede no asignada"
            }

        txtDireccionSede.text =
            sessionManager.obtenerDireccion().ifBlank {
                "Dirección no disponible"
            }


        val btnCerrarSesion =
            view.findViewById<View>(R.id.btnCerrarSesion)

        btnCerrarSesion.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Cerrar sesión")
                .setMessage("¿Deseas cerrar tu sesión en S.I.G.A. PRO?")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Cerrar sesión") { _ , _ ->

                    sessionManager.cerrarSesion()

                    val intent = Intent(
                        requireContext(),
                        LoginActivity::class.java
                    )

                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK

                    startActivity(intent)
                }
                .show()
        }


    }


}