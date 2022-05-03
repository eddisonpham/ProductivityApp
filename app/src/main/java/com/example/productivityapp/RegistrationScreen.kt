package com.example.productivityapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class RegistrationScreen : AppCompatActivity() {
    private lateinit var btnReg: Button
    private lateinit var regEmail: EditText
    private lateinit var regFull: EditText
    private lateinit var regPhone: EditText
    private lateinit var regConfirm: EditText
    private lateinit var regPass: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_screen)
        btnReg=findViewById(R.id.btnReg)
        regEmail=findViewById(R.id.regEmail)
        regFull=findViewById(R.id.regFull)
        regPhone=findViewById(R.id.regPhone)
        regConfirm=findViewById(R.id.regConfirm)
        regPass=findViewById(R.id.regPass)
        btnReg.setOnClickListener {
            var firstname = regFull.text.toString().split(" ")[0]
            var lastname = regFull.text.toString().split(" ")[1]
            if (!(regFull.text.toString().split(" ").size>=2)){
                var toast = Toast.makeText(this@RegistrationScreen, "Enter your FIRST and LAST name.", Toast.LENGTH_SHORT).show()
            }else if (!regEmail.text.toString().contains(".")&&!regEmail.text.toString().contains("@")){
                var toast = Toast.makeText(this@RegistrationScreen, "Invalid email.", Toast.LENGTH_SHORT).show()
            }else if (regPass.text.toString().length<8||regPass.text.toString().length>20){
                var toast = Toast.makeText(this@RegistrationScreen, "Password must be 8-20 characters.", Toast.LENGTH_SHORT).show()
            }else if (!regPass.text.toString().equals(regConfirm.text.toString())){
                var toast = Toast.makeText(this@RegistrationScreen, "Passwords do not match.", Toast.LENGTH_SHORT).show()
            }else{
                var URL = "http://${IPCONFIG.IP}/ProductivityApp/register.php?firstname=${firstname}&lastname=${lastname}&email=${regEmail.text}&password=${regPass.text}&phonenum=${regPhone.text}&taskscompleted=0&level=Alpha"
                var requestQ = Volley.newRequestQueue(this)
                var stringRequest = StringRequest(Request.Method.GET, URL, Response.Listener { response->
                    var toast = Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show()
                    if (response.toString().equals("Congratulations! The registration process was successful!")){
                        Person.email=regEmail.text.toString()
                        Person.firstname=firstname
                        Person.lastname=lastname
                        Person.phonenumber=regPhone.text.toString()
                        Person.password=regPass.text.toString()
                        Person.taskscompleted=0
                        Person.level="Alpha"
                        var intent = Intent(this,ToDoList::class.java)
                        startActivity(intent)
                    }

                }, Response.ErrorListener { error->
                    var toast = Toast.makeText(this, error.stackTraceToString(), Toast.LENGTH_SHORT).show()
                })
                requestQ.add(stringRequest)
            }
        }
    }
}