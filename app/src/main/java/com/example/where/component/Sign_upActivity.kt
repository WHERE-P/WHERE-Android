package com.example.where.component

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.where.R
import com.example.where.api.Service
import com.example.where.api.UserRequest
import com.example.where.api.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Sign_upActivity : AppCompatActivity() {
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextConfirmPassword: EditText

    private lateinit var retrofit: Retrofit
    private lateinit var apiService: Service

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        editTextEmail = findViewById(R.id.join_email)
        editTextPassword = findViewById(R.id.join_password)
        editTextConfirmPassword = findViewById(R.id.reconfirm)

        // Retrofit 초기화
        retrofit = Retrofit.Builder()
            .baseUrl("http://13.124.44.106:8080/")  // 서버의 주소로 변경해야 합니다.
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(Service::class.java)

        val buttonSignup: Button = findViewById(R.id.join_button)
        buttonSignup.setOnClickListener {
            signup()
        }
        val textViewLogin: TextView = findViewById(R.id.login_text)
        textViewLogin.setOnClickListener {
            toLoginActivity()
        }

    }

    private fun signup() {
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()
        val confirmPassword = editTextConfirmPassword.text.toString().trim()



        // 비밀번호 확인
        if (password != confirmPassword) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }


        apiService.signup(UserRequest(email, password, confirmPassword)).enqueue(object :
            Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val signupResponse = response.body()
                    // 회원가입 성공 처리
                    if (signupResponse != null) {
                        // 회원가입이 성공적으로 완료되었을 때의 처리를 작성합니다.
                        Toast.makeText(this@Sign_upActivity, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        toLoginActivity()
                    }
                } else {
                    Log.d("signup", response.message())
                    Log.d("signup", response.code().toString())
                    // 회원가입 실패 처리
                    Toast.makeText(this@Sign_upActivity, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                // 네트워크 오류 등 예외 발생 시 처리
                Toast.makeText(this@Sign_upActivity, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun toLoginActivity(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}