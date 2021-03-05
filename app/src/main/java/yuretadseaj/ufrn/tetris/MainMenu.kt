package yuretadseaj.ufrn.tetris

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import yuretadseaj.ufrn.tetris.databinding.ActivityMainMenuBinding

class MainMenu : AppCompatActivity() {

    lateinit var binding: ActivityMainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_menu)

        val gameScreenIntent = Intent(this, MainActivity::class.java)
        val settingsScreenIntent = Intent(this, Settings::class.java)

        binding.apply {
            btnNewGame.setOnClickListener {
                startActivity(gameScreenIntent)
            }
            btnSettings.setOnClickListener {
                startActivity(settingsScreenIntent)
            }
        }
    }
}