package com.example.lab10

import android.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import com.example.lab10.databinding.ActivitySearch2Binding


class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearch2Binding
    private var allCars: ArrayList<Car> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearch2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(com.example.lab10.R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        allCars = intent.getSerializableExtra("ALL_CARS") as ArrayList<Car>

        setupAdapters()
        setupListeners()
    }

    private fun setupAdapters() {
        // 1. Унікальні марки та моделі для AutoCompleteTextView (Set)
        val brands = allCars.map { it.brand }.distinct().sorted()
        val models = allCars.map { it.model }.distinct().sorted()

        binding.actvBrand.setAdapter(
            ArrayAdapter(
                this,
                R.layout.simple_dropdown_item_1line,
                brands
            )
        )
        binding.actvModel.setAdapter(ArrayAdapter(this, R.layout.simple_dropdown_item_1line, models))

        // 2. Спінер років (тільки ті, що є в наявності)
        val yearsFrom = allCars.map { it.year }.distinct().sorted()
        val yearsTo = allCars.map { it.year }.distinct().sorted().reversed()
        val yearAdapterFrom = ArrayAdapter(this, R.layout.simple_spinner_item, yearsFrom)
        val yearAdapterTo = ArrayAdapter(this, R.layout.simple_spinner_item, yearsTo)
        binding.spinnerYearFrom.adapter = yearAdapterFrom
        binding.spinnerYearTo.adapter = yearAdapterTo

        // 3. Статичні ціни (на порядок вище)
        val pricesFrom = listOf("0", "100", "1000", "10000", "100000", "1000000")
        val pricesTo = pricesFrom.reversed()
        val priceAdapterFrom = ArrayAdapter(this, R.layout.simple_spinner_item, pricesFrom)
        val priceAdapterTo = ArrayAdapter(this, R.layout.simple_spinner_item, pricesTo)
        binding.spinnerPriceFrom.adapter = priceAdapterFrom
        binding.spinnerPriceTo.adapter = priceAdapterTo
    }

    private fun setupListeners() {
        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { performFiltering() }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        binding.actvBrand.addTextChangedListener(watcher)
        binding.actvModel.addTextChangedListener(watcher)

        // Спінери також повинні викликати фільтрацію
        val spinnerListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) { performFiltering() }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        binding.spinnerYearFrom.onItemSelectedListener = spinnerListener
        binding.spinnerYearTo.onItemSelectedListener = spinnerListener
        binding.spinnerPriceFrom.onItemSelectedListener = spinnerListener
        binding.spinnerPriceTo.onItemSelectedListener = spinnerListener
    }

    private fun performFiltering() {
        val brandQuery = binding.actvBrand.text.toString().trim()
        val modelQuery = binding.actvModel.text.toString().trim()

        // Отримуємо значення років зі спінерів
        // Спінери заповнені Int, тому можемо безпечно кастити
        val yearFrom = binding.spinnerYearFrom.selectedItem as Int
        val yearTo = binding.spinnerYearTo.selectedItem as Int

        // Отримуємо значення цін (перетворюємо зі String "1000" в Long)
        val priceFrom = binding.spinnerPriceFrom.selectedItem.toString().toLongOrNull() ?: 0L
        val priceTo = binding.spinnerPriceTo.selectedItem.toString().toLongOrNull() ?: Long.MAX_VALUE

        val filtered = allCars.filter { car ->
            // 1. Фільтр по тексту (Марка та Модель)
            val matchBrand = brandQuery.isEmpty() || car.brand.contains(brandQuery, ignoreCase = true)
            val matchModel = modelQuery.isEmpty() || car.model.contains(modelQuery, ignoreCase = true)

            // 2. Фільтр по роках (включно)
            val matchYear = car.year in yearFrom..yearTo

            // 3. Фільтр по ціні
            // Очищаємо рядок ціни "$95,000" -> "95000"
            val cleanPrice = car.cost.replace(Regex("[^0-9]"), "").toLongOrNull() ?: 0L
            val matchPrice = cleanPrice in priceFrom..priceTo

            matchBrand && matchModel && matchYear && matchPrice
        }

        // Умова: заповнено хоча б одне текстове поле АБО змінено спінери (за бажанням)
        // Якщо хочете, щоб пошук працював навіть тільки за роками, змініть hasInput
        val hasInput = brandQuery.isNotEmpty() || modelQuery.isNotEmpty() || true

        binding.btnMatches.apply {
            isEnabled = filtered.isNotEmpty() // Кнопка активна, якщо є результати
            text = "Matches: ${filtered.size}"
            setOnClickListener {
                val resultIntent = Intent()
                resultIntent.putExtra("FILTERED_LIST", ArrayList(filtered))
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }
}