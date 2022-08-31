package com.flytbase.calculator.data

import android.util.Log
import com.flytbase.calculator.utils.Constants
import com.flytbase.calculator.view.ui.frahments.LoginFragment
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class FirestoreClass {
    private val mFireStore = FirebaseFirestore.getInstance()

    fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    data class Password(val password: String)

    fun getPasswordDetails(fragment: LoginFragment) {
//        val password = Password("ssshsh")
//        mFireStore.collection(Constants.PASSWORD)
//            .document()
//            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
//            .set(password, SetOptions.merge())
        // Here we pass the collection name from which we wants the data.
        mFireStore.collection(Constants.PASSWORD)
            // The document id to get the Fields of user.
            // .document()
            .get()
            .addOnSuccessListener { document ->

                Log.i(fragment.javaClass.simpleName, document.toString())

                val passwordList = ArrayList<String>()

                for (i in document.documents) {
                    val pass: String = i.get(Constants.PASSWORD) as String
                    //  i.toObject(Password::class.java)?.let { passwordList.add(it.password) }
                    passwordList.add(pass)
                }

                fragment.successPasswordListFromFireStore(passwordList)
            }
            .addOnFailureListener { e ->

            }
    }

    fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    suspend fun uploadHistory(list: LinkedList<String>){
        Log.d("ss21"," indsii")
        // Here we pass the collection name from which we wants the data.
        val update = HashMap<String,String>()
        for(i in 0..9){
            if(i < list.size)
            update["$i"] = list.get(i)
            else
                update["$i"] = ""
        }
        mFireStore.collection(Constants.HISTORY)
            // The document id to get the Fields of user.
            .document(getCurrentUserID())
            .set(update, SetOptions.merge())
            .addOnSuccessListener { Log.d("ss21s","${it.toString()}") }
            .addOnFailureListener {
                Log.d("ss21","${it.printStackTrace()}")
            }
    }

    suspend fun getHistory(): Task<DocumentSnapshot> {
      return  mFireStore.collection(Constants.HISTORY)
            .document(getCurrentUserID())
            .get()
//            .addOnSuccessListener {
//                val passwordList = ArrayList<String>()
//
//                for (i in it.documents) {
//                    val pass: String = i.get(Constants.PASSWORD) as String
//                    //  i.toObject(Password::class.java)?.let { passwordList.add(it.password) }
//                    passwordList.add(pass)
//                }
//            }
    }


}