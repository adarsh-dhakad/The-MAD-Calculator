package com.flytbase.calculator.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flytbase.calculator.data.FirestoreClass
import com.flytbase.calculator.utils.Operation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

class MainActivityViewModel : ViewModel() {

    private val firestoreClass = FirestoreClass()
    var errorLiveData = MutableLiveData<Boolean>()
    var ansLiveData = MutableLiveData<Float>()
    var ansListLiveData = MutableLiveData<List<String>>()
    private var list = LinkedList<String>()

    fun add(input: String, output: String) {
        list.addFirst("$input#$output")
        if (list.size > 10) {
            list.removeLast()
        }
        ansListLiveData.value = list

        viewModelScope.launch(Dispatchers.IO) {
            if (firestoreClass.getCurrentUser() != null)
                firestoreClass.uploadHistory(list)
        }
    }

    fun getHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = LinkedList<String>()
            val f = firestoreClass.getHistory().await()
            for (i in 0..9) {
                val item = f.get("$i").toString()
                if (item != "" && item != "null")
                    list.add(item)
            }
            withContext(Dispatchers.Main) {
                this@MainActivityViewModel.list = list
                ansListLiveData.value = list
            }
        }
    }

    @Throws(Exception::class)
    fun evaluate(expression: String) {
        viewModelScope.launch(Dispatchers.IO) {
            var ans: Float = Float.MAX_VALUE
            var error = true
            try {
                ans = Operation().evaluate(expression)
                error = false
            }catch (e:Exception){
                error = true
            }
            withContext(Dispatchers.Main) {
                if (!error) {
                    ansLiveData.value = ans
                    errorLiveData.value = error
                }else{
                    errorLiveData.value = error
                }
            }

        }
    }


}