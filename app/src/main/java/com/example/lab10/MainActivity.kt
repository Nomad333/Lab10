package com.example.lab10

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lab10.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var carAdapter: CarAdapter
    private var allCars = listOf<Car>() // Ваш повний список

    private val searchLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // Отримуємо відфільтрований список із SearchActivity
            val filteredCars = result.data?.getSerializableExtra("FILTERED_LIST") as? ArrayList<Car>
            filteredCars?.let {
                carAdapter.updateList(it) // Оновлюємо список у ресайклері
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.btnSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            intent.putExtra("ALL_CARS", ArrayList(allCars))
            searchLauncher.launch(intent)
        }

        // Кнопка Reset повертає повний список
        binding.btnReset.setOnClickListener {
            carAdapter.updateList(allCars)
        }
    }
}