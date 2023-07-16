package com.example.where.component

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.where.R
import com.example.where.api.LoginRequest
import com.example.where.api.LoginResponse
import com.example.where.api.Service
import com.example.where.local.LocalDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var retrofit: Retrofit
    private lateinit var apiService: Service

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextEmail = findViewById(R.id.login_email)
        editTextPassword = findViewById(R.id.login_password)

        retrofit = Retrofit.Builder()
            .baseUrl("http://13.124.44.106:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(Service::class.java)

        val buttonSignup: Button = findViewById(R.id.login_button)
        buttonSignup.setOnClickListener {
            login(this)
            finish()
        }


    }

    private fun login(context: Context) {
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()

        // 로그인 요청 보내기
        apiService.login(LoginRequest(email, password)).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()

                    if (loginResponse != null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            LocalDataSource(context).saveToken(
                                access = loginResponse.access,
                                refresh = loginResponse.refresh
                            )
                        }
                        Toast.makeText(this@LoginActivity, "로그인이 완료되었습니다.", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(this@LoginActivity, MyPageActivity::class.java)
                        intent.putExtra("email", email)
                        val homeIntent = Intent(this@LoginActivity, HomeActivity::class.java)
                        homeIntent.putExtra("email", email)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    Log.d("signup", response.message())
                    Log.d("signup", response.code().toString())
                    Toast.makeText(this@LoginActivity, "로그인에 실패했습니다.", Toast.LENGTH_SHORT)
                        .show()
                }

            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("error", t.message.toString())
                // 네트워크 오류 등 예외 발생 시 처리
                Toast.makeText(this@LoginActivity, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
    }