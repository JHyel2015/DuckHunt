package com.epnfis.duckhuntrichard

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.epnfis.duckhuntrichard.models.User

import kotlinx.android.synthetic.main.fragment_user.view.*

class MyUserRecyclerViewAdapter(
    private val mValues: List<User>
) : RecyclerView.Adapter<MyUserRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as User
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        val pos = position + 1
        holder.textViewPosition.text = "$pos°"
        holder.textViewDucks.text = item.ducks.toString()
        holder.textViewNick.text = item.nick
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val textViewPosition: TextView = mView.textViewPosition
        val textViewDucks: TextView = mView.textViewDucks
        val textViewNick: TextView = mView.textViewNick

        override fun toString(): String {
            return super.toString() + " '" + textViewDucks.text + " " + textViewNick.text + "'"
        }
    }
}
