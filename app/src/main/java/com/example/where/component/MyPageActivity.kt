package com.example.where.component

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.where.R
import com.example.where.api.NameResponse
import com.example.where.api.Service
import com.example.where.local.LocalDataSource
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyPageActivity : AppCompatActivity() {

    private lateinit var retrofit: Retrofit
    private lateinit var whereWhoTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        whereWhoTextView = findViewById(R.id.where_who)

        val url = intent.getStringExtra("url")
        if (url == "https://me-qr.com/wCoL1bkW") {
            whereWhoTextView.text = getString(R.string.absent)
            whereWhoTextView.setTextColor(ContextCompat.getColor(this, R.color.out))

        }else if(url == "https://me-qr.com/I1JE8LNu"){
            whereWhoTextView.text = getString(R.string.auditorium)
            whereWhoTextView.setTextColor(ContextCompat.getColor(this, R.color.yellow))
        } else {
            whereWhoTextView.text = getString(R.string.attendance)
            whereWhoTextView.setTextColor(ContextCompat.getColor(this, R.color.green))
        }

        val logoutButton = findViewById<ImageView>(R.id.rogout_button)
        logoutButton.setOnClickListener {
            // 로그아웃 버튼 클릭 시 처리할 내용
            Toast.makeText(this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // 현재 액티비티를 종료하여 뒤로 가기 시에 다시 마이페이지로 돌아가지 않도록 합니다.
        }

        val context = this@MyPageActivity
        val username = intent.getStringExtra("email")

        CoroutineScope(Dispatchers.IO).launch {
            val accessToken = LocalDataSource(context).getAccess().first()

            retrofit = Retrofit.Builder()
                .baseUrl("http://13.124.44.106:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(Service::class.java)


            val usernameTextView = findViewById<TextView>(R.id.username)
            val namestudentTextView = findViewById<TextView>(R.id.student_name)
            val usernames = ""
            val call = service.getusername(accessToken = accessToken, username = usernames)

            call.enqueue(object : Callback<NameResponse> {
                override fun onResponse(
                    call: Call<NameResponse>,
                    response: Response<NameResponse>
                ) {
                    if (response.isSuccessful) {
                        val nameResponse = response.body()
                        val name = nameResponse?.name

                        usernameTextView.text = name // 텍스트뷰에 값을 설정
                        namestudentTextView.text = name

                    } else {
                        Log.d("MyPageActivity", "API 호출 실패")
                    }
                }

                override fun onFailure(call: Call<NameResponse>, t: Throwable) {
                    Log.d("MyPageActivity", "API 호출 실패: ${t.message}")
                }
            })
        }
            val parts = username?.split("@")
            if (parts != null) {
                if (parts.size == 2) {
                    val username = parts[0]
                    val grade_num = username.substring(0, 1) // 첫 번째 문자인 3
                    val class_num = username.substring(1, 2) // 두 번째 문자인 3
                    val student_num = username.substring(2) // 세 번째 문자부터 끝까지인 10
                    val user_num = username.substring(0)
                    // 이제 이 변수들을 필요에 따라 사용할 수 있습니다.
                    // 예를 들어, XML에서 아이디 이름을 grade_num_text_view, class_num_text_view, student_num_text_view로 가진 TextView에 값을 설정하는 경우:
                    val gradeNumTextView = findViewById<TextView>(R.id.grade_num)
                    gradeNumTextView.text = grade_num

                    val classNumTextView = findViewById<TextView>(R.id.class_num)
                    classNumTextView.text = class_num

                    val studentNumTextView = findViewById<TextView>(R.id.student_num)
                    studentNumTextView.text = student_num

                    val userNumTextView = findViewById<TextView>(R.id.user_num)
                    userNumTextView.text = user_num
                }
            }


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.mine)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("email", username) // 이메일 값을 전달
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
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
        bottomNavigationView.menu.findItem(R.id.cleass_list)?.isChecked = true

        //startQRScan()

    }
//    private fun startQRScan() {
//        val qrCodeScan = QRCodeScane(this)
//        qrCodeScan.startQRScan { value ->
//            when (value) {
//                1 -> {
//                    val attendanceText = resources.getString(R.string.attendance)
//                    val greenColor = ContextCompat.getColor(this, R.color.green)
//                    whereWhoTextView.text = attendanceText
//                    whereWhoTextView.setTextColor(greenColor)
//                }
//                2 -> {
//                    val absentText = resources.getString(R.string.absent)
//                    val outColor = ContextCompat.getColor(this, R.color.out)
//                    whereWhoTextView.text = absentText
//                    whereWhoTextView.setTextColor(outColor)
//                }
//                3 -> {
//                    val auditoriumText = resources.getString(R.string.auditorium)
//                    val yellowColor = ContextCompat.getColor(this, R.color.yellow)
//                    whereWhoTextView.text = auditoriumText
//                    whereWhoTextView.setTextColor(yellowColor)
//                }
//                else -> {
//                    // Handle other cases
//                }
//            }
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK) {
//            val value = data?.getIntExtra("value", 0)
//            if (value != null) {
//                // Call the callback function with the value
//                qrCodeScanCallback?.invoke(value)
//            }
//        }
//    }


}