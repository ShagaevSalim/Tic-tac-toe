package com.shag.tic_tac_toe

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.SystemClock
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shag.tic_tac_toe.SettingsActivity.Companion.PREF_LVL
import com.shag.tic_tac_toe.SettingsActivity.Companion.PREF_RULES
import com.shag.tic_tac_toe.SettingsActivity.Companion.PREF_SOUND_LEVEL
import com.shag.tic_tac_toe.SettingsActivity.SettingsInfo
import com.shag.tic_tac_toe.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding

    private lateinit var gameField: Array<Array<String>>

    private lateinit var settingsInfo: SettingsActivity.SettingsInfo

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)

        binding.toPopupMenu.setOnClickListener{
            showPopupMenu()
        }
        binding.toGameClose.setOnClickListener{

            onBackPressedDispatcher.onBackPressed()

        }



        binding.cell11.setOnClickListener{
            makeStepOfUser(0,0)
        }
        binding.cell12.setOnClickListener{
            makeStepOfUser(0,1)

        }
        binding.cell13.setOnClickListener{
            makeStepOfUser(0,2)

        }
        binding.cell21.setOnClickListener{
            makeStepOfUser(1,0)

        }
        binding.cell22.setOnClickListener{
            makeStepOfUser(1,1)

        }
        binding.cell23.setOnClickListener{
            makeStepOfUser(1,2)

        }
        binding.cell31.setOnClickListener{
            makeStepOfUser(2,0)

        }
        binding.cell32.setOnClickListener{
            makeStepOfUser(2,1)

        }
        binding.cell33.setOnClickListener{
            makeStepOfUser(2,2)

        }





        setContentView(binding.root)

        val time = intent.getLongExtra(MainActivity.EXTRA_TIME, 0)
        val gameField = intent.getStringExtra(MainActivity.EXTRA_GAME_FIELD)

        if(gameField != null && time != 0L && gameField != "" ){
            restartGame(time, gameField)
        }else{
            initGameField()
        }

        settingsInfo = getSettingsInfo()

        mediaPlayer = MediaPlayer.create(this,R.raw.themel)
        mediaPlayer.isLooping = true
        setVolumeMediaPlayer(settingsInfo.soundLvl)

        mediaPlayer.start()
        binding.chronometr.start()
    }

    override fun onDestroy(){
        super.onDestroy()
        mediaPlayer.release()
    }

    override fun onStop(){
        super.onStop()
        mediaPlayer.release()

    }

    private fun setVolumeMediaPlayer(soundValue: Int){
        val volume = soundValue/100.0
        mediaPlayer.setVolume(volume.toFloat(), volume.toFloat())
    }

    private fun initGameField(){
        gameField = Array(3){Array(3){" "} }
    }

    private fun makeStep(row: Int, column: Int, symbol: String){
        gameField[row][column] = symbol

        makeStepUI("$row$column", symbol)
    }

    private fun makeStepUI(position: String, symbol: String){
        val resId = when(symbol){
            "x" -> R.drawable.ic_cross
            "0" -> R.drawable.ic_zero
            else -> return
        }

        when(position){
            "00" -> binding.cell11.setImageResource(resId)
            "01" -> binding.cell12.setImageResource(resId)
            "02" -> binding.cell13.setImageResource(resId)
            "10" -> binding.cell21.setImageResource(resId)
            "11" -> binding.cell22.setImageResource(resId)
            "12" -> binding.cell23.setImageResource(resId)
            "20" -> binding.cell31.setImageResource(resId)
            "21" -> binding.cell32.setImageResource(resId)
            "22" -> binding.cell33.setImageResource(resId)
        }
    }

    private fun makeStepOfUser(row: Int, column: Int){

        if(isEmptyField(row,column)){
            makeStep(row,column,"X")

            val status = checkGameField(row, column, "X")
            if (status.status){
                showGameStatus(STATUS_PLAYER_WIN)
                return
            }

            if(!isFilledGameField()){
                makeStepOfAI()

                val statusAI = checkGameField(row, column, "0")
                if(statusAI.status){
                    showGameStatus(STATUS_PLAYER_LOSE)
                    return
                }

                if(!isFilledGameField()){
                    showGameStatus(STATUS_PLAYER_DRAW)
                    return
                }
            }else{
                showGameStatus(STATUS_PLAYER_DRAW)
                return
            }



        }else{
            Toast.makeText(this,"Поле уже заполнено", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isEmptyField(row: Int, column: Int): Boolean{
        return gameField[row][column] == " "
    }

    private fun makeStepOfAI(){
        when(settingsInfo.lvl){
            0 -> makeStepOfAIEasyLvl()
            1 -> makeStepOfAIMediumLvl()
            3 -> makeStepOfAIHardLvl()
        }
    }

    private fun makeStepOfAIHardLvl() {
        TODO("Not yet implemented")
    }

    private fun makeStepOfAIMediumLvl() {
        TODO("Not yet implemented")
    }

    private fun makeStepOfAIEasyLvl(){
        var randRow = 0
        var randColumn = 0
        do {
            randRow = (0..2).random()
            randColumn = (0..2).random()
        } while(!isEmptyField(randRow, randColumn))

        makeStep(randRow, randColumn, "0")
    }


    private fun checkGameField(x: Int, y: Int, symbol: String): StatusInfo{
        var row = 0
        var column = 0
        var leftDiagonal = 0
        var rightDiagonal = 0
        var n = gameField.size

        for(i in 0..2){
            if(gameField[x][i] == symbol)
                column++
            if(gameField[i][y]==symbol)
                row++
            if(gameField[i][i] == symbol)
                leftDiagonal++
            if(gameField[i][n-i-1]==symbol)
                rightDiagonal++
        }

        return when(settingsInfo.rules){
            1 ->{
                if(column==n)
                    StatusInfo(true,symbol)
                else
                    StatusInfo(false, "")
            }
            2 ->{
                if(row==n)
                    StatusInfo(true,symbol)
                else
                    StatusInfo(false, "")
            }
            3 ->{
                if(column==n || row == n)
                    StatusInfo(true,symbol)
                else
                    StatusInfo(false, "")
            }
            4 ->{
                return if(leftDiagonal == n || rightDiagonal == n)
                    StatusInfo(true,symbol)
                else
                    StatusInfo(false, "")
            }
            5 ->{
                return if(column==n || leftDiagonal == n || rightDiagonal == n)
                    StatusInfo(true,symbol)
                else
                    StatusInfo(false, "")
            }
            6 ->{
                return if(row == n || leftDiagonal == n || rightDiagonal == n)
                    StatusInfo(true,symbol)
                else
                    StatusInfo(false, "")
            }
            7 ->{
                return if(column==n || row == n || leftDiagonal == n || rightDiagonal == n)
                    StatusInfo(true,symbol)
                else
                    StatusInfo(false, "")
            }
            else ->  StatusInfo(false, "")
        }



    }

    data class StatusInfo(
        val status: Boolean,
        val side: String,
    )

    private fun showGameStatus(status: Int){
        val dialog = Dialog(this, R.style.Base_Theme_Tictactoe)
        with(dialog){
            window?.setBackgroundDrawable(ColorDrawable(Color.argb(50, 0,0,0)))
            setContentView(R.layout.dialog_popup_status_game)
            setCancelable(true)
        }

        val image = dialog.findViewById<ImageView>(R.id.dialogImage)
        val text = dialog.findViewById<TextView>(R.id.dialogText)
        val button = dialog.findViewById<TextView>(R.id.dialogOk)

        button.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        when(status){
            STATUS_PLAYER_WIN ->{
                image.setImageResource(R.drawable.ic_win)
                text.text = getString(R.string.dialog_status_win)
            }
            STATUS_PLAYER_LOSE ->{
                image.setImageResource(R.drawable.ic_lose)
                text.text = getString(R.string.dialog_status_lose)
            }
            STATUS_PLAYER_DRAW ->{
                image.setImageResource(R.drawable.ic_draw)
                text.text = getString(R.string.dialog_status_draw)
            }
        }

        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
    if(requestCode== REQUEST_POPUP_MENU){
        if(resultCode== RESULT_OK){
            settingsInfo = getSettingsInfo()

            mediaPlayer = MediaPlayer.create(this,R.raw.themel)
            mediaPlayer.isLooping = true
            setVolumeMediaPlayer(settingsInfo.soundLvl)

            mediaPlayer.start()
        }
        else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    }

    private fun showPopupMenu(){
        val dialog = Dialog(this, R.style.Base_Theme_Tictactoe)
        with(dialog){
            window?.setBackgroundDrawable(ColorDrawable(Color.argb(50, 0,0,0)))
            setContentView(R.layout.dialog_popup_menu)
            setCancelable(true)
        }

        val toContinue = dialog.findViewById<ImageView>(R.id.dialogContinue)
        val toSettings = dialog.findViewById<TextView>(R.id.dialogSettings)
        val toExit = dialog.findViewById<TextView>(R.id.dialogExit)

        toContinue.setOnClickListener{
            dialog.hide()
        }

        toSettings.setOnClickListener{
            dialog.hide()
            val intent = Intent(this, SettingsActivity::class.java)
            startActivityForResult(intent, REQUEST_POPUP_MENU)

        }

        toExit.setOnClickListener{
            val elapsedMills = SystemClock.elapsedRealtime() - binding.chronometr.base
            val gameField = convertGameFieldToString(gameField)
            saveGame(elapsedMills, gameField)
            dialog.dismiss()
            onBackPressedDispatcher.onBackPressed()
        }

        dialog.show()

    }


    private fun isFilledGameField():Boolean{
        gameField.forEach { strings ->
            if(strings.find { it == " " } != null){
                return false
            }
        }
        return true
    }




    private fun convertGameFieldToString(gameField: Array<Array<String>>): String{
        val tmpArray = arrayListOf<String>()
        gameField.forEach { tmpArray.add(it.joinToString { ";" }) }
        return tmpArray.joinToString { "\n" }
    }

    private fun saveGame(time: Long, gameField: String){
        with(getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE).edit()){
            putLong(PREF_TIME,time)
            putString(PREF_GAME_FIELD, gameField)
            apply()
        }
    }

    private fun restartGame(time: Long, gameField: String){
        binding.chronometr.base = SystemClock.elapsedRealtime()-time

        this.gameField = arrayOf()

        val rows = gameField.split("\n")
        rows.forEach{
            val columns = it.split(";")
            this.gameField += columns.toTypedArray()
        }

        this.gameField.forEachIndexed{indexRow, strings ->
            strings.forEachIndexed{indexColumn, s->
                makeStep(indexRow, indexColumn, this.gameField[indexRow][indexColumn])
            }
        }

    }

    private fun getSettingsInfo(): SettingsInfo
    {
        with(getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE)){
            val soundLvl = getInt(PREF_SOUND_LEVEL, 50)
            val lvl = getInt(PREF_LVL, 0)
            val rules = getInt(PREF_RULES, 7)

            return SettingsInfo(soundLvl, lvl, rules)
        }
    }


    companion object{
        const val STATUS_PLAYER_WIN = 1
        const val STATUS_PLAYER_LOSE = 2
        const val STATUS_PLAYER_DRAW = 3

        const val PREF_TIME = "pref_time"
        const val PREF_GAME_FIELD = "pref_game_field"


        const val REQUEST_POPUP_MENU = 123
    }
}