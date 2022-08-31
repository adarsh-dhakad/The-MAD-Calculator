package com.flytbase.calculator.view.ui.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.flytbase.calculator.R
import com.flytbase.calculator.databinding.ActivityMainBinding
import com.flytbase.calculator.view.adapter.InputOutputAdapter
import com.flytbase.calculator.view.ui.frahments.CalculatorFragment
import com.flytbase.calculator.viewmodel.MainActivityViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fm: FragmentManager = this.supportFragmentManager
        val fragment = CalculatorFragment()
        fm.beginTransaction()
            //      .add(R.id.main_contenier, fragment).commit()
            .replace(R.id.main_contenier,fragment).commit()

    }
}
