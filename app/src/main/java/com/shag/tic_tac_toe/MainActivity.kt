package com.shag.tic_tac_toe

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.shag.tic_tac_toe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.toNewGame.setOnClickListener{
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
        binding.toContinueGame.setOnClickListener{
            val gameInfo = getInfoAboutGame()
            val intent = Intent(this, GameActivity::class.java).apply{
                putExtra(EXTRA_TIME, gameInfo.time)
                putExtra(EXTRA_GAME_FIELD, gameInfo.gameField)
            }
            startActivity(intent)
        }
        binding.toSettings.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }


        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        println("onCreate()")
    }

    override fun onDestroy() {
        super.onDestroy()

        println("onDestroy()")
    }

    override fun onStop() {
        super.onStop()

        println("onStop()")
    }

    override fun onStart() {
        super.onStart()
        println("onStart()")
    }

    override fun onRestart() {
        super.onRestart()
        println("onRestart()")
    }

    override fun onResume() {
        super.onResume()
        println("onResume()")
    }

    override fun onPause() {
        super.onPause()
        println("onPause()")
    }

    private fun getInfoAboutGame(): InfoGame{
        with(getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE)){
            val time = getLong(GameActivity.PREF_TIME, 0L)
            val gameField = getString(GameActivity.PREF_GAME_FIELD, "")

            return if(gameField!=null){
                InfoGame(time, gameField)
            }else{
                InfoGame(0, "")
            }
        }
    }
    data class InfoGame(
        val time: Long,
        val gameField: String,
    )

    companion object{
        const val EXTRA_TIME = "extra_time"
        const val EXTRA_GAME_FIELD = "extra_game_field"
    }

}