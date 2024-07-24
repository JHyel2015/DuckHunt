package com.epnfis.duckhuntrichard

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Path
import android.graphics.Point
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.epnfis.duckhuntrichard.common.Constantes
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.activity_game.*
import java.util.*


class GameActivity : AppCompatActivity() {

    var counter = 0
    var widthScreen = 0
    var heightScreen = 0
    var random = Random()
    var gameOver = false
    lateinit var id: String
    lateinit var nick: String
    lateinit var db: FirebaseFirestore
    lateinit var animator: ObjectAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        db = FirebaseFirestore.getInstance()
//        val settings = FirebaseFirestoreSettings.Builder()
//            .setTimestampsInSnapshotsEnabled(true)
//            .build()
//        db.firestoreSettings = settings
        MediaPlayer.create(this, R.raw.duck_hunt_intro).start()
        imageViewDogLaugh.visibility = View.GONE
        initViewComponents()
        initScreen()
        moveDuck()
        initCount()
    }

    private fun initViewComponents() {
        val extras = intent.extras
        nick = extras?.get(Constantes().EXTRA_NICK).toString()
        id = extras?.get(Constantes().EXTRA_ID).toString()

        // val typeface = Typeface.createFromAsset(assets, "")

        val typeface = ResourcesCompat.getFont(this, R.font.pixel)
        textViewNick.typeface = typeface
        textViewNick.text = nick

        textViewCounterDucks.typeface = typeface
        textViewTimer.typeface = typeface
    }

    private fun initCount() {
        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                textViewTimer.text = secondsRemaining.toString() + "s"
            }

            override fun onFinish() {
                gameOver = true
                textViewTimer.text = "0s"
                MediaPlayer.create(this@GameActivity, R.raw.game_over).start()
                showDialogGameOver()
                saveResultFireStore()
            }
        }.start()
    }

    private fun saveResultFireStore() {
        db.collection("users")
            .document(id)
            .update("ducks", counter)
    }

    private fun showDialogGameOver() {
        // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
        val builder: AlertDialog.Builder? = this.let {
            AlertDialog.Builder(it)

        }

        // 2. Chain together various setter methods to set the dialog characteristics
        builder?.setMessage("Has conseguido cazar: " + counter + " patos")?.setTitle("Game Over")
        builder?.setPositiveButton("Reiniciar",
            DialogInterface.OnClickListener { _, _ ->
                // User clicked OK button
                counter = 0
                textViewCounterDucks.text = "0"
                gameOver = false
                initCount()
                moveDuck()
            })
        builder?.setNegativeButton("Ver ranking",
            DialogInterface.OnClickListener { dialog, _ ->
                // User cancelled the dialog
                dialog.dismiss()
                val intent = Intent(this, RankingActivity::class.java)
                intent.putExtra(Constantes().EXTRA_NICK, nick)
                intent.putExtra(Constantes().EXTRA_ID, id)
                startActivity(intent)
            })
        builder?.setOnCancelListener { dialog ->
            val intent = Intent(this, RankingActivity::class.java)
            startActivity(intent)
        }

        // 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
        val dialog: AlertDialog? = builder?.create()
        dialog?.show()
    }

    fun onClickImageViewDuck(view: View) {
        imageViewDogLaugh.visibility = View.GONE
        MediaPlayer.create(this, R.raw.gunshot).start()
        if (!gameOver) {
            counter++
            textViewCounterDucks.text = counter.toString()
            imageViewDuck.setImageResource(R.drawable.duck_clicked)
            animator.pause()
            Handler().postDelayed({
                imageViewDogLaugh.visibility = View.VISIBLE
                imageViewDogLaugh.setImageResource(R.drawable.duck_caught)
            },250)
            Handler().postDelayed({
                imageViewDogLaugh.visibility = View.GONE
                imageViewDogLaugh.setImageResource(R.drawable.dog_laugh)
                imageViewDuck.setImageResource(R.drawable.duck_move)
                moveDuck()
            }, 500)
        }
    }

    fun onClickParentView( view: View ) {
        imageViewDogLaugh.visibility = View.VISIBLE
        MediaPlayer.create(this, R.raw.gunshot).start()
        MediaPlayer.create(this, R.raw.dog_laugh).start()
    }

    private fun moveDuck() {
        val min = 0
        val maxX = widthScreen - imageViewDuck.width
        val maxY = heightScreen - imageViewDuck.height

        val randomX0 = random.nextInt(((maxX - min) + 1) + min)
        val randomY0 = random.nextInt(((maxY - min) + 1) + min)
        val randomX = random.nextInt(((maxX - min) + 1) + min)
        val randomY = random.nextInt(((maxY - min) + 1) + min)
        val randomX2 = random.nextInt(((maxX - min) + 1) + min)
        val randomY2 = random.nextInt(((maxY - min) + 1) + min)

        imageViewDuck.translationX = randomX0.toFloat()
        imageViewDuck.translationY = randomY0.toFloat()
        val path = Path().apply {
            // arcTo(0f, 0f, randomX.toFloat(), randomY.toFloat(), randomStartAngle.toFloat(), randomSweepAngle.toFloat(), true)
            cubicTo( randomX0.toFloat(), randomY0.toFloat(), randomX.toFloat(), randomY.toFloat(), randomX2.toFloat(), randomY2.toFloat())
        }
        animator = ObjectAnimator.ofFloat(imageViewDuck, "x", "y", path).apply {
            duration = 1300
            start()
            addListener(object: AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    moveDuck()
                }
            })
        }
        // animator.repeatCount = ObjectAnimator.INFINITE

    }

    private fun initScreen() {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        widthScreen = size.x
        heightScreen = size.y
        random = Random()
    }
}
