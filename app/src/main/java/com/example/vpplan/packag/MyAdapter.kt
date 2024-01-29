// Остановился в этом файле


package com.example.vpplan.packag

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.vpplan.MainActivity2
import com.example.vpplan.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MyAdapter(listMain:ArrayList<ListItem>,contextM: Context) : RecyclerView.Adapter<MyAdapter.MyHolder>(){ // MyA() - создание конструктора и передача списка listmain
    var listarray = listMain
    val context = contextM

    class MyHolder(itemView: View,contextV: Context) : RecyclerView.ViewHolder(itemView) {
       //val layout = itemView.findViewById<LinearLayout>(R.id.tr)
        var daysLef:Int = 0



        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        val tvDT = itemView.findViewById<TextView>(R.id.teV)
        val context = contextV


        fun setData(item:ListItem){

            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val currentDate = Calendar.getInstance()
            val userDate = Calendar.getInstance()
            userDate.time = inputFormat.parse(item.desc)

            val difference = userDate.timeInMillis - currentDate.timeInMillis
            val daysLeft = difference / (24 * 60 * 60 * 1000)
            daysLef = daysLeft.toInt()

            tvTitle.text = item.title
            if(daysLeft >= 0){
                tvDT.text = "Осталось: ${daysLeft.toString()} д."}else{tvDT.text = "Событие прошло"}

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
        if(holder.daysLef < 5) {
            holder.itemView.setBackgroundResource(R.drawable.red)

        } else {
            if(holder.daysLef in 5..10) {
                holder.itemView.setBackgroundResource(R.drawable.ye)
                //holder.itemView.setBackgroundColor(Color.YELLOW)
            } else {
                holder.itemView.setBackgroundResource(R.drawable.blue)
            }
        }

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