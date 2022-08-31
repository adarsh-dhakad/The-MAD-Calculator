package com.flytbase.calculator.view.ui.frahments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.flytbase.calculator.data.FirestoreClass
import com.flytbase.calculator.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {


    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showBackButton()
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        requireActivity().supportFragmentManager.popBackStack(
            "Later Transaction",
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        return super.onOptionsItemSelected(item)
    }

    private fun showBackButton() {
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(binding.toolbar.toolbar)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
            (activity as AppCompatActivity).title = "Login"
        }

        val usernameEditText = binding.username
        val loginButton = binding.login
        val loadingProgressBar = binding.loading


        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                if (binding.username.text.length >= 3) {
                  binding.login.isEnabled = true
                } else {
                       binding.username.error = "Enter 3 digit Username"
                }
            }
        }
        usernameEditText.addTextChangedListener(afterTextChangedListener)

        loginButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            FirestoreClass().getPasswordDetails(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun successPasswordListFromFireStore(passwordList: ArrayList<String>) {
        val username = binding.username.text.toString()
        var password = passwordList[0]
        if(username.contains("a") || username.contains('b') || username.contains('r')|| username.contains('y')|| username.contains('u')|| username.contains('b')|| username.contains('x')){
            password = passwordList[1]
        }
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword("$username@gmail.com", password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                   Toast.makeText(requireContext(),"Welcome",Toast.LENGTH_LONG).show()
                } else {
                    auth.createUserWithEmailAndPassword("$username@gmail.com", password)
                        .addOnCompleteListener(requireActivity()) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("register fragment", "createUserWithEmail:success")
                                val user = auth.currentUser
                                updateUiWithUser(user)
                            } else {
                                binding.loading.visibility = View.GONE
                                Log.w("register fragment", "createUserWithEmail:failure", task.exception)
                                Toast.makeText(requireContext(), "Authentication failed.${task.exception}",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
    }

    private fun updateUiWithUser(user: FirebaseUser?) {
        requireActivity().supportFragmentManager.popBackStack(
            "Later Transaction",
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }
}