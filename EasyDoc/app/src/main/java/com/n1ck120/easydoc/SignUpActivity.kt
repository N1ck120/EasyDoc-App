package com.n1ck120.easydoc

import android.content.Intent
import android.os.Bundle
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
import android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnSignup = findViewById<Button>(R.id.btnSignup)
        val card1 = findViewById<MaterialCardView>(R.id.Eye1)
        val eye1 = findViewById<ImageView>(R.id.imageViewEye1)
        val card2 = findViewById<MaterialCardView>(R.id.Eye2)
        val eye2 = findViewById<ImageView>(R.id.imageViewEye2)
        val pass1 = findViewById<EditText>(R.id.pass1)
        val pass2 = findViewById<EditText>(R.id.pass2)

        btnLogin.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        fun setupCardCorners(cardView: MaterialCardView) {
            val shapeAppearanceModel = ShapeAppearanceModel.builder()
                .setTopLeftCorner(CornerFamily.ROUNDED, 10f)
                .setTopRightCorner(CornerFamily.ROUNDED, 58f)
                .setBottomLeftCorner(CornerFamily.ROUNDED, 10f)
                .setBottomRightCorner(CornerFamily.ROUNDED, 58f)
                .build()

            cardView.shapeAppearanceModel = shapeAppearanceModel
        }

        setupCardCorners(card1)
        setupCardCorners(card2)

        card1.setOnClickListener {
            card1.isChecked = !card1.isChecked
            if (card1.isChecked){
                eye1.setImageResource(R.drawable.baseline_visibility_24)
                pass1.inputType = TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }else{
                eye1.setImageResource(R.drawable.baseline_visibility_off_24)
                pass1.inputType = TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_PASSWORD
            }
        }

        card2.setOnClickListener {
            card2.isChecked = !card2.isChecked
            if (card2.isChecked){
                eye2.setImageResource(R.drawable.baseline_visibility_24)
                pass2.inputType = TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }else{
                eye2.setImageResource(R.drawable.baseline_visibility_off_24)
                pass2.inputType = TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_PASSWORD
            }
        }

        //Verifica o tema salvo no datastore e troca caso necessario
        val dataStore = SettingsDataStore.getDataStorePrefs(this)
        val key = intPreferencesKey("theme")
        lifecycleScope.launch {
            AppCompatDelegate.setDefaultNightMode(dataStore.data.first()[key] ?: MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}