package com.epnfis.duckhuntrichard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.epnfis.duckhuntrichard.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import java.util.ArrayList

class UserRankingFragment : Fragment() {

    private var columnCount = 1
    lateinit var userList: List<User>
    lateinit var db: FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_list, container, false)

        db = FirebaseFirestore.getInstance()

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                db.collection("users")
                    .orderBy("ducks", Query.Direction.DESCENDING)
                    .limit(10)
                    .get()
                    .addOnCompleteListener{ result ->
                        userList = ArrayList()
                        for (document in result.result!!) {
                            val userItem = User(document.get("nick").toString(), document.get("ducks").toString().toInt())
                            (userList as ArrayList<User>).add(userItem)
                            adapter = MyUserRecyclerViewAdapter(userList)
                        }
                    }
            }
        }
        return view
    }
}
