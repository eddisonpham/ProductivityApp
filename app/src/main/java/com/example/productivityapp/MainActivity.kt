package com.example.productivityapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley


class MainActivity : AppCompatActivity() {
    private lateinit var btnLogin:Button
    private lateinit var edtEmail:EditText
    private lateinit var edtPassword:EditText
    private lateinit var registerNow:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnLogin=findViewById(R.id.btnLogin)
        edtEmail=findViewById(R.id.regEmail)
        edtPassword=findViewById(R.id.edtPassword)
        registerNow=findViewById(R.id.registerNow)
        btnLogin.setOnClickListener {
            var URL = "http://${IPCONFIG.IP}/ProductivityApp/login.php?email=${edtEmail.text}&password=${edtPassword.text}"
            var requestQ = Volley.newRequestQueue(this)
            var stringRequest = StringRequest(Request.Method.GET, URL, Response.Listener{response->
                if (response.toString().equals("The user does not exist")){
                    var toast = Toast.makeText(this, "Invalid credentials.", Toast.LENGTH_SHORT).show()
                }else{
                    var USERURL = "http://${IPCONFIG.IP}/ProductivityApp/getUserInformationWithEmail.php?email=${edtEmail.text}"
                    var requestQ2 = Volley.newRequestQueue(this)
                    var jsonRequest = JsonObjectRequest(Request.Method.GET,USERURL, null, Response.Listener { response2->
                        Person.email=response2.getString("email")
                        Person.firstname=response2.getString("firstname")
                        Person.lastname=response2.getString("lastname")
                        Person.phonenumber=response2.getString("phonenum")
                        Person.password=response2.getString("password")
                        Person.taskscompleted=response2.getInt("taskscompleted")
                        Person.level=response2.getString("level")
                        var intent = Intent(this,ToDoList::class.java)
                        startActivity(intent)
                    }, Response.ErrorListener { error->

                    })
                    requestQ2.add(jsonRequest)
                    var toast = Toast.makeText(this, "Successfully logged in!", Toast.LENGTH_SHORT).show()

                }
            },Response.ErrorListener { error->
                var toast = Toast.makeText(this, error.stackTraceToString(), Toast.LENGTH_SHORT).show()
                Log.i("tag",edtEmail.text.toString()+" "+edtPassword.text.toString())
            })
            requestQ.add(stringRequest)
        }
        registerNow.setOnClickListener {
            var intent = Intent(this@MainActivity, RegistrationScreen::class.java)
            startActivity(intent)
        }

    }
}