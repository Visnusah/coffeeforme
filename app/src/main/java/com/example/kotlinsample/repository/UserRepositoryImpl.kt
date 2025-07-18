package com.example.kotlinsample.repository

import com.example.kotlinsample.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.callbackFlow

class UserRepositoryImpl: UserRepository {
    val auth: FirebaseAuth= FirebaseAuth.getInstance()
    val database: FirebaseDatabase= FirebaseDatabase.getInstance()
    val ref: DatabaseReference=database.reference.child("users")

    override fun login(
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    callback(true,"login successful")
                }
                else{
                    callback(false,"${it.exception?.message}")
                }
            }
    }

    override fun register(
        email: String,
        password: String,
        callback: (Boolean, String, String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { us->
                if(us.isSuccessful){
                    callback(true,"Registration complete",
                        "${auth.currentUser?.uid}")

                }
                else{
                    callback(false,"${us.exception?.message}","")
                }

            }

    }

    override fun forgetPassword(
        email: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Password reset email sent to $email.")
                } else {
                    val errorMessage = task.exception?.localizedMessage ?: "An error occurred."
                    callback(false, errorMessage)
                }
            }

    }

    override fun editProfile(
        userId: String,
        updateData: Map<String, Any>,
        callback: (Boolean, String) -> Unit
    ) {
        val ref = FirebaseDatabase.getInstance().getReference("users").child(userId)
        ref.updateChildren(updateData).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, "Profile updated successfully")
            } else {
                callback(false, task.exception?.message ?: "Failed to update")
            }
        }
    }


    override fun getProfile(
        userId: String,
        callback: (Map<String, Any>) -> Unit
    ) {
        ref.child(userId).get().addOnSuccessListener {
            val map = it.value as? Map<String, Any>
            callback(map ?: emptyMap())
        }.addOnFailureListener {
            callback(emptyMap())
        }
    }




    override fun addUserToDatabase(
        userId: String,
        model: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(userId).setValue(model).addOnCompleteListener {
            if (it.isSuccessful){
                callback(true,"user added")
            }
            else{
                callback(false,"${it.exception?.message}")
            }
        }

    }

    override fun getUserFromDatabase(
        userId: String,
        callback: (Boolean, String, UserModel?) -> Unit
    ) {
        ref.child(userId)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        var users=snapshot.getValue(UserModel::class.java)
                        callback(true,"Fetched",users)
                    }
                    else{
                        callback(false,"cant",null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, "Database error: ${error.message}", null)
                }
            })

    }

    override fun logout(callback: (Boolean, String) -> Unit) {
        try {
            auth.signOut()
            callback(true, "logout")
        }catch ( e: Exception){
            callback(false,"${e.message}")
        }

        }



    override fun deleteAcc(
        userId: String,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(userId).removeValue().addOnCompleteListener {
            if (it.isSuccessful){
                callback(true,"user removed")
            }
            else{
                callback(false,"${it.exception?.message}")
            }

        }
    }
    override fun getAllUsers(callback: (Boolean, String, List<UserModel>) -> Unit) {
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = mutableListOf<UserModel>()
                if (snapshot.exists()) {
                    for (userSnap in snapshot.children) {
                        val user = userSnap.getValue(UserModel::class.java)
                        user?.let { userList.add(it) }
                    }
                    callback(true, "All users fetched successfully", userList)
                } else {
                    callback(false, "No users found", emptyList())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, "Failed to fetch users: ${error.message}", emptyList())
            }
        })
    }
}