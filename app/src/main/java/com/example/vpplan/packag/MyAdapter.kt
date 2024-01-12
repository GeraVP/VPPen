package com.example.vpplan.packag

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.vpplan.MainActivity2
import com.example.vpplan.R

class MyAdapter(listMain:ArrayList<ListItem>,contextM: Context) : RecyclerView.Adapter<MyAdapter.MyHolder>(){ // MyA() - создание конструктора и передача списка listmain
    var listarray = listMain
    val context = contextM
    class MyHolder(itemView: View,contextV: Context) : RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        val context = contextV
        fun setData(item:ListItem){
            tvTitle.text = item.title
            itemView.setOnClickListener {
                val intent = Intent(context,MainActivity2::class.java).apply {
                    putExtra(MyIntentConstants.I_title,item.title)
                    putExtra(MyIntentConstants.I_desc,item.desc)
                    putExtra(MyIntentConstants.I_URI,item.uri)
                    putExtra(MyIntentConstants.I_ID,item.id)
                }
                context.startActivity(intent)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
       val inflater = LayoutInflater.from(parent.context)
        return MyHolder(inflater.inflate(R.layout.rcitme,parent,false),context)
    }
    override fun getItemCount(): Int {
       return listarray.size
    }
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.setData(listarray.get(position))
    }
    fun upDateAdapter(listItems:List<ListItem>){
        listarray.clear()
        listarray.addAll(listItems)
        notifyDataSetChanged()
    }
    fun removeItem(pos:Int, dbManager: MyDBManager){
        dbManager.removeItemFromDb(listarray[pos].id.toString())
        listarray.removeAt(pos)
        notifyItemRangeChanged(0,listarray.size)
        notifyItemRemoved(pos)
    }
}