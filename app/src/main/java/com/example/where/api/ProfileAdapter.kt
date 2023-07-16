package com.example.where.api

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.where.R

class ProfileAdapter(private val context: Context) : RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {
    private var dataList: List<Profile> = emptyList()
    private var filteredDataList: List<Profile> = emptyList()
    private var profiles: MutableList<Profile> = mutableListOf()
    private var filteredProfiles: MutableList<Profile> = mutableListOf() // 검색 결과를 담을 새로운 리스트
    private var searchQuery: String = ""
    var datas = mutableListOf<Profile>()
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtName: TextView = itemView.findViewById(R.id.personName)
        private val txtGrade: TextView = itemView.findViewById(R.id.usergrade)
        private val txtWhere: TextView = itemView.findViewById(R.id.where)

        fun bind(item: Profile) {
            txtName.text = item.name
            txtGrade.text = item.group
            txtWhere.text = item.state
        }
    }
    fun filterData(query: String) {
        searchQuery = query
        filteredDataList = dataList.filter { profile ->
            profile.name.equals(searchQuery, ignoreCase = true)
        }
        notifyDataSetChanged()
    }
    fun filter(query: String) {
        filteredProfiles.clear() // 검색 결과 리스트 초기화

        if (query.isEmpty()) {
            filteredProfiles.addAll(profiles) // 검색어가 없으면 전체 프로필을 추가
        } else {
            val filteredList = profiles.filter { profile ->
                profile.name.contains(query, ignoreCase = true)
            }
            filteredProfiles.addAll(filteredList) // 검색어와 일치하는 프로필을 추가
        }

        notifyDataSetChanged() // 어댑터에 데이터 변경을 알림
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_user_info, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileAdapter.ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    override fun getItemCount(): Int = datas.size
}