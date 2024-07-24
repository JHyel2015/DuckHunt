package com.epnfis.duckhuntrichard

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.epnfis.duckhuntrichard.common.Constantes

import kotlinx.android.synthetic.main.activity_ranking.*
import kotlinx.android.synthetic.main.content_ranking.*

class RankingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.beginTransaction()
            .add(fragmentContainer.id, UserRankingFragment())
            .commit()
    }

    fun onClickFabButton( view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

}
