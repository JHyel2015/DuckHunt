package com.epnfis.duckhuntrichard

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.epnfis.duckhuntrichard.common.Constantes
import com.epnfis.duckhuntrichard.models.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    lateinit var nick: String
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        db = FirebaseFirestore.getInstance()

        MediaPlayer.create(this, R.raw.title_screen).start()
        // Create a new user with a first and last name
        /*
        val user = hashMapOf(
            "nick" to "Tefa",
            "ducks" to 0
        )

        // Add a new document with a generated ID
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d("DuckHunt", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("DuckHunt", "Error adding document", e)
            }
         */

        val typeface = ResourcesCompat.getFont(this, R.font.pixel)
        editTextNick.typeface = typeface
        buttonStart.typeface = typeface
    }

    fun onClickButtonStart( view: View ) {
        nick = editTextNick.text.toString()
        if (nick.isEmpty()) {
            editTextNick.error = "Escriba un nick valido"
        } else if (nick.length < 4) {
            editTextNick.error = "Debe tener al menos 4 caracteres"
        } else {
            addNickAndStart()

        }
    }

    private fun addNickAndStart() {
        db.collection("users").whereEqualTo("nick", nick)
            .get()
            .addOnSuccessListener { result ->
                if (result.size() > 0) {
                    editTextNick.error = "El nick no esta disponible"
                } else {
                    addNickToFireStore()
                }
            }

    }

    private fun addNickToFireStore() {
        val newUser = User(nick, 0)
        db.collection("users")
            .add(newUser)
            .addOnSuccessListener { result ->
                editTextNick.text.clear()
                val intent = Intent( this, GameActivity::class.java)
                intent.putExtra(Constantes().EXTRA_NICK, nick)
                intent.putExtra(Constantes().EXTRA_ID, result.id)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Log.w("DuckHunt", "Error adding document", e)
            }

    }
}
