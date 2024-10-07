package com.shag.tic_tac_toe

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.shag.tic_tac_toe.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private var currentSoundLevel = 0
    private var currentLvl = 0
    private var currentRules = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)

        val data = getSettingsInfo()

        currentSoundLevel = data.soundLvl
        currentRules = data.rules
        currentLvl = data.lvl

        when(currentRules){
            1 -> binding.checkboxVertical.isChecked = true
            2 -> binding.checkboxHorizontal.isChecked = true
            3 -> {
                binding.checkboxVertical.isChecked = true
                binding.checkboxHorizontal.isChecked = true
            }
            4 -> binding.checkboxDiagonal.isChecked = true
            5 -> {
                binding.checkboxDiagonal.isChecked = true
                binding.checkboxVertical.isChecked = true
            }
            6 -> {
                binding.checkboxDiagonal.isChecked = true
                binding.checkboxHorizontal.isChecked = true
            }
            7 -> {
                binding.checkboxVertical.isChecked = true
                binding.checkboxHorizontal.isChecked = true
                binding.checkboxDiagonal.isChecked = true
            }
        }

        if(currentLvl == 0){
            binding.prevLvl.visibility = View.INVISIBLE
        }else if(currentLvl == 2){
            binding.nextLvl.visibility = View.INVISIBLE
        }

        binding.infoLevel.text = resources.getStringArray(R.array.game_level)[currentLvl]
        binding.soundBar.progress = currentSoundLevel

        binding.toBack.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }


        binding.prevLvl.setOnClickListener{
            currentLvl--

            if(currentLvl == 0){
                binding.prevLvl.visibility = View.INVISIBLE
            }else if(currentLvl == 1){
                binding.nextLvl.visibility = View.VISIBLE
            }

            binding.infoLevel.text = resources.getStringArray(R.array.game_level)[currentLvl]


            updateLvl(currentLvl)
        }

        binding.nextLvl.setOnClickListener{
            currentLvl++

            if(currentLvl==1){
                binding.prevLvl.visibility = View.VISIBLE
            }else if (currentLvl == 2){
                binding.nextLvl.visibility = View.INVISIBLE
            }

            binding.infoLevel.text = resources.getStringArray(R.array.game_level)[currentLvl]

            updateLvl(currentLvl)
        }

        binding.soundBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, value: Int, p2: Boolean) {
                currentSoundLevel = value
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                updateSoundValue(currentSoundLevel)
            }
        } )

        binding.checkboxVertical.setOnCheckedChangeListener{_, isChecked ->
            if(isChecked){
                currentRules++
            }else{
                currentRules--
            }



            updateRules(currentRules)
        }

        binding.checkboxHorizontal.setOnCheckedChangeListener{_, isChecked ->
            if(isChecked){
                currentRules+=2
            }else{
                currentRules-=2
            }

            updateRules(currentRules)

        }

        binding.checkboxDiagonal.setOnCheckedChangeListener{_, isChecked ->
            if(isChecked){
                currentRules+=4
            }else{
                currentRules-=4
            }

            updateRules(currentRules)

        }


        setContentView(binding.root)
    }

    private fun updateSoundValue(value: Int){
        with(getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE).edit()){
            putInt(PREF_SOUND_LEVEL, value)
            apply()
        }
        setResult((RESULT_OK))
    }

    private fun updateLvl(lvl: Int){
        with(getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE).edit()){
            putInt(PREF_LVL, lvl)
            apply()
        }
        setResult((RESULT_OK))

    }

    private fun updateRules(rules: Int){
        with(getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE).edit()){
            putInt(PREF_RULES, rules)
            apply()
        }
        setResult((RESULT_OK))

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

    data class SettingsInfo(
        val soundLvl: Int,
        val lvl: Int,
        val rules: Int,
    )

    companion object{
        const val PREF_SOUND_LEVEL = "pref_sound_level"
        const val PREF_LVL = "pref_lvl"
        const val PREF_RULES = "pref_rules"
    }

}