package com.example.lab10

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import com.example.lab10.databinding.ActivitySearch2Binding


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearch2Binding
    private var filteredList = listOf<Car>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearch2Binding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val allCars = intent.getSerializableExtra("ALL_CARS") as ArrayList<Car>

        binding.btnMatches.setOnClickListener {
            // 3. Повертаємо результат
            val resultIntent = Intent()
            resultIntent.putExtra("FILTERED_LIST", ArrayList(filteredList))
            setResult(RESULT_OK, resultIntent)
            finish() // Закриваємо екран пошуку
        }

        binding.actvBrand.doAfterTextChanged {

        }

    }
}