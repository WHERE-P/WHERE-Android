package com.example.where.api

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.where.R

data class UserWhereDataAdapter(private val context: Context): RecyclerView.Adapter<UserWhereDataAdapter.ViewHolder>() {
    var datas = mutableListOf<UserWhereData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_user_info, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtperson: TextView = itemView.findViewById(R.id.personName)
        val txtgrade: TextView = itemView.findViewById(R.id.usergrade)
        fun bind(item: UserWhereData) {
            txtperson.text = item.user.name
            txtgrade.text = item.user.grade
        }
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = datas[position]
        val personName = data.user.name
        val usergrade = "${data.user.grade} ${data.user.group}반 ${data.user.number}번"

        holder.txtperson.text = personName
        holder.txtgrade.text = usergrade
    }

    override fun getItemCount(): Int = datas.size
}