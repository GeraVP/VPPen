package com.example.vpplan.packag

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.vpplan.R

class MyAdapter(listMain:ArrayList<String>) : RecyclerView.Adapter<MyAdapter.MyHolder>(){ // MyA() - создание конструктора и передача списка listmain
    var listarray = listMain
    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        fun setData(title:String){
            tvTitle.text = title
        }
    } // Создаётся 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder { // Функция, где мы создаём шаблон на основе xml и передаем myholder 2
       val inflater = LayoutInflater.from(parent.context)
        return MyHolder(inflater.inflate(R.layout.rcitme,parent,false))
        // inflater - специальный класс который надувает xml файл и превращает его в объект который будет нарисован на экране
    }

    override fun getItemCount(): Int { // Сообщает адаптеру сколько элемннтов ему необходимо будет нарисовать или подключить к rv эл в списке 1
       return listarray.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.setData(listarray.get(position))
    } // Запускается когда myholder создан. Заполнение шаблона значением позиции  4
    fun upDateAdapter(listItems:List<String>){
        listarray.clear()
        listarray.addAll(listItems)
        notifyDataSetChanged()
    } // Берёт массив listarray, очищает, добавлеяет новый массив, сообщает адаптеру что данные изменились
}