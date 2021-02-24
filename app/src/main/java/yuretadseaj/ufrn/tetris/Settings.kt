package yuretadseaj.ufrn.tetris

import android.content.Context
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import yuretadseaj.ufrn.tetris.databinding.ActivitySettingsBinding

class Settings : AppCompatActivity() {

    lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)

        val settings = getSharedPreferences("${R.string.app_name}_settings", Context.MODE_PRIVATE)
        val settingsEditor = settings.edit()
        val currentDifficulty = settings.getString("difficulty", "")
        val currentPiecesVariation = settings.getInt("pieces_variation", 8)

        binding.apply {
            editTextPiecesVariation.setText(currentPiecesVariation.toString())
            when(currentDifficulty) {
                "easy" -> radioButtonEasy.isChecked = true
                "medium" -> radioButtonMedium.isChecked = true
                "hard" -> radioButtonHard.isChecked = true
            }
            radioGroupDifficulty.setOnCheckedChangeListener { _: RadioGroup, option: Int ->
                val difficulty = when(option) {
                    R.id.radioButtonEasy -> "easy"
                    R.id.radioButtonMedium -> "medium"
                    R.id.radioButtonHard -> "hard"
                    else -> "medium"
                }
                settingsEditor.putString("difficulty", difficulty).apply()
            }
            btnSavePiecesVariation.setOnClickListener {
                var piecesVariation = editTextPiecesVariation.text.toString().toInt()
                piecesVariation = if (piecesVariation > 8) 8 else piecesVariation
                piecesVariation = if (piecesVariation < 1) 1 else piecesVariation
                settingsEditor.putInt("pieces_variation", piecesVariation).apply()
                val newPiecesVariation = settings.getInt("pieces_variation", 8)
                editTextPiecesVariation.setText(newPiecesVariation.toString())
                Toast.makeText(this@Settings, "Pieces Variation changed to $newPiecesVariation", Toast.LENGTH_LONG).show()
            }
        }

    }
}