package com.example.seefood.ui.main.dashboard

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.seefood.R
import org.w3c.dom.Text

class CustomAdapter(private val context: Context, val posts: MutableList<String> ) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(s: String) {
            itemView.findViewById<TextView>(R.id.itemName).text = s

        }
        val tv: TextView
        init {
            tv = itemView.findViewById(R.id.itemName)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(posts[position])

    }

    override fun getItemCount()= posts.size


}
