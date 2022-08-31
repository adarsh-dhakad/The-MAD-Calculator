package com.flytbase.calculator.view.ui.frahments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.flytbase.calculator.R
import com.flytbase.calculator.data.FirestoreClass
import com.flytbase.calculator.databinding.FragmentCalculatorBinding
import com.flytbase.calculator.view.adapter.InputOutputAdapter
import com.flytbase.calculator.viewmodel.MainActivityViewModel


class CalculatorFragment : Fragment() {
    private lateinit var binding: FragmentCalculatorBinding
    private val viewModel: MainActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (FirestoreClass().getCurrentUser() != null){
            viewModel.getHistory()
        }
        binding = FragmentCalculatorBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity is AppCompatActivity) {
            //  title for fragment
            (activity as AppCompatActivity).setSupportActionBar(binding!!.toolbar.toolbar)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
            (activity as AppCompatActivity).setTitle("Calculator")
        }

        var ans = ""

        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        val adapter = InputOutputAdapter(this)
        binding.rvHistory.adapter = adapter

        // ans live data
        viewModel.ansListLiveData.observeForever {
            if (it.isNotEmpty()) {
                adapter.itemList(it)
            }
        }

        // we are observe if user doing some wrong operations
        viewModel.errorLiveData.observeForever {
            if(it){
                binding.tvAnsView.text = "Error"
            }
        }

        viewModel.ansLiveData.observeForever {
            if (it != 0F) {
                ans = it.toString()
                var i = ans.indexOf(".")
                if(i+2 < ans.length){
                   ans = ans.substring(0,i+3)
                }else{
                   if(ans[i+1] == '0') {
                       ans = ans.substring(0, i)
                   }
                }
                binding.tvAnsView.text = ans
            }
        }
        binding.btnHistory.setOnClickListener {
            binding.rvHistory.visibility = View.VISIBLE
        }
        val input = binding.etInput
        val editText = binding.etInput
        editText.afterTextChanged {
                viewModel.evaluate(input.text.toString())
        }
        editText.setOnEditorActionListener { v, actionId, event ->
            var handled = false

//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                Toast.makeText(this," ${binding.etInput.text.toString()}",Toast.LENGTH_LONG).show()
//                handled = true
//            }
            if (event.action == KeyEvent.ACTION_DOWN) {
                try {
                    viewModel.evaluate(input.text.toString())
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), " Wrong Input ", Toast.LENGTH_LONG).show()
                }
                viewModel.add(input.text.toString(), ans)
                binding.tvAnsView.text = ""
                binding.etInput.setText(ans)
                editText.setSelection(editText.length())
                handled = true
            }
            handled
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_calculator_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_login -> {
                setHasOptionsMenu(false)
                val fm: FragmentManager = requireActivity().supportFragmentManager
                val fragment = LoginFragment()
                fm.beginTransaction()
                    .replace(R.id.main_contenier, fragment)
                    .addToBackStack("Later Transaction").commit()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}


fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}