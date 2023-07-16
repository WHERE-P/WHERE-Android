package com.example.where.component

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.where.R
import com.example.where.api.NameResponse
import com.example.where.api.Profile
import com.example.where.api.ProfileAdapter
import com.example.where.api.Service
import com.example.where.api.User
import com.example.where.api.UserWhereData
import com.example.where.api.UserWhereDataAdapter
import com.example.where.local.LocalDataSource
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var maincolumn: RecyclerView
    private lateinit var adapters: UserWhereDataAdapter
    private lateinit var adapter: ProfileAdapter
    private lateinit var originalDataList: MutableList<UserWhereData>
    private lateinit var retrofit: Retrofit
    private lateinit var spinnerYear: Spinner

    private var userEmail: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        spinnerYear = findViewById(R.id.spinner_year)

        // 스피너 데이터 초기화
        val spinnerAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.user_state,
            android.R.layout.simple_spinner_item
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerYear.adapter = spinnerAdapter


        maincolumn = findViewById(R.id.list)

        originalDataList = mutableListOf() // 초기화

        val intent = intent
        userEmail = intent.getStringExtra("email")
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val context = this@HomeActivity
        CoroutineScope(Dispatchers.IO).launch {
            val accessToken = LocalDataSource(context).getAccess().first()

            retrofit = Retrofit.Builder()
                .baseUrl("http://13.124.44.106:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            val service = retrofit.create(Service::class.java)

            val usermainTextView = findViewById<TextView>(R.id.main_person)
            val usermainname = ""
            val call = service.getusername(accessToken = accessToken, username = usermainname)

            call.enqueue(object : Callback<NameResponse>{
                override fun onResponse(
                    call: Call<NameResponse>,
                    response: Response<NameResponse>
                ) {
                    if (response.isSuccessful){
                        val nameResponse = response.body()
                        val name = nameResponse?.name

                        usermainTextView.text = name
                    }else{
                        Log.d("HomeActivity", "API 호출 실패")
                    }
                }
                override fun onFailure(call: Call<NameResponse>, t: Throwable) {
                    Log.d("HomeActivity", "API 호출 실패: ${t.message}")
                }
            })
        }
        val usernumber = intent.getStringExtra("email")
        val parts = usernumber?.split("@")
        if (parts != null) {
            if (parts.size == 2) {
                val usernumber = parts[0]
                val student_user_num = usernumber.substring(0)
                val lastmeassedTextView = findViewById<TextView>(R.id.lastmeassed)
                lastmeassedTextView.text = usernumber
            }
        }

        initRecycler()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.homenavi)
        bottomNavigationView.setOnItemSelectedListener {item->
            when (item.itemId){
                R.id.home -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.qrscane -> {
                    val intent = Intent(this, QrCodeScanActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.cleass_list -> {
                    val intent = Intent(this, MyPageActivity::class.java)
                    intent.putExtra("email", usernumber)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
        bottomNavigationView.menu.findItem(R.id.home)?.isChecked = true

        val imageView = findViewById<ImageView>(R.id.filter)
        imageView.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this)
            val bottomSheetView = layoutInflater.inflate(R.layout.bottomsheetlayout, null)


            val searchView = bottomSheetView.findViewById<SearchView>(R.id.searchView)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return true
                }
            })

            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()
        }

    }
    private fun initRecycler(){
        val datas = mutableListOf<Profile>()
        datas.add(Profile(name = "강경민", group = "3학년 1반 1번", state = "출석 완료"))
        datas.add(Profile(name = "곽희상", group = "3학년 1반 2번", state = "출석 완료"))
        datas.add(Profile(name = "김도현", group = "3학년 1반 3번", state = "출석 완료"))
        datas.add(Profile(name = "김동현", group = "3학년 1반 4번", state = "외출중"))
        datas.add(Profile(name = "김민준", group = "3학년 1반 5번", state = "외출중"))
        datas.add(Profile(name = "김시훈", group = "3학년 1반 6번", state = "결석"))
        datas.add(Profile(name = "김준", group = "3학년 1반 7번", state = "외출중"))
        datas.add(Profile(name = "노가성", group = "3학년 1반 8번", state = "출석 완료"))
        datas.add(Profile(name = "박지예", group = "3학년 1반 9번", state = "외출중"))
        datas.add(Profile(name = "변찬우", group = "3학년 1반 10번", state = "출석 완료"))
        datas.add(Profile(name = "선민재", group = "3학년 1반 11번", state = "출석 완료"))
        datas.add(Profile(name = "손정민", group = "3학년 1반 12번", state = "출석 완료"))
        datas.add(Profile(name = "양시준", group = "3학년 1반 13번", state = "출석 완료"))
        datas.add(Profile(name = "유시온", group = "3학년 1반 14번", state = "출석 완료"))
        datas.add(Profile(name = "이준", group = "3학년 1반 15번", state = "외출중"))
        datas.add(Profile(name = "정은성", group = "3학년 1반 16번", state = "출석 완료"))
        datas.add(Profile(name = "조현서", group = "3학년 1반 17번", state = "출석 완료"))
        datas.add(Profile(name = "홍세현", group = "3학년 1반 18번", state = "외출중"))

        val profileAdapter = ProfileAdapter(this)
        profileAdapter.datas = datas

        maincolumn = findViewById(R.id.list)
        maincolumn.adapter = profileAdapter
        maincolumn.layoutManager = LinearLayoutManager(this)

        adapter = ProfileAdapter(this)

//        adapter = UserWhereDataAdapter(this)
//        maincolumn.adapter = adapter
//        maincolumn.layoutManager = LinearLayoutManager(this)
//
//        val service = retrofit.create(Service::class.java)
//
//        val grade = 1
//        val group = 2
//        val name = "John"
//        val state = "absent"
//        val context = this
//
//        CoroutineScope(Dispatchers.IO).launch {
//            val accessToken = LocalDataSource(context).getAccess().first()
//
//            val call = service.getUsersByState(
//                accessToken = accessToken,
//                grade = grade,
//                group = group,
//                name = name,
//                state = state
//            )
//
//            val interceptor =
//            call.enqueue(object : Callback<List<UserWhereData>> {
//                override fun onResponse(call: Call<List<UserWhereData>>, response: Response<List<UserWhereData>>) {
//                    if (response.isSuccessful) {
//                        val userWhereDataList = response.body()
//                        Log.d("success", "success")
//                        if (userWhereDataList != null) {
//                            Log.d("success", "not null ${userWhereDataList.size}")
//                            adapter.datas.addAll(userWhereDataList)
//                            maincolumn.adapter = adapter
//                            adapter.notifyDataSetChanged()
//                        }
//                    } else {
//
//                        Log.d("HomeActivity", "API 호출 실패")
//                    }
//                }
//
//                override fun onFailure(call: Call<List<UserWhereData>>, t: Throwable) {
//                    // API 호출 실패 처리
//                    Log.d("HomeActivity", "API 호출 실패: ${t.message}")
//                }
//            })
//        }
//        adapter.datas.addAll(originalDataList)
//        adapter.notifyDataSetChanged()
    }

}




