package com.example.seefood.ui.main.dashboard

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.seefood.R
import org.w3c.dom.Text

class CustomAdapter
    (private val context: Context,
     val posts: MutableList<String> ,
     private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>(){

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        fun bind(s: String) {
            itemView.findViewById<TextView>(R.id.itemName).text = s

        }
        val tv: TextView
        init {
            tv = itemView.findViewById(R.id.itemName)
            itemView.setOnClickListener(this)
        }

        // this is the onclick listener that will tell the app what to do when clicked
        // we will need to extract info through firebase either through an id or a name

        // this will forward the click to the screen that will hold the data which will be the
        // dashboard fragment

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
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


    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }


}
