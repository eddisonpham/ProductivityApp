package com.example.productivityapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.text.SimpleDateFormat
import java.util.*

class MakeNewTask : AppCompatActivity(),DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener{
    private lateinit var edtTDTitle: EditText
    private lateinit var edtTDDesc: EditText
    private lateinit var edtStarttime: TextView
    private lateinit var spinnerTaskType: Spinner
    private lateinit var btnCreateTask: Button
    var taskType = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_new_task)
        edtTDTitle = findViewById(R.id.edtTDTitle)
        edtTDDesc = findViewById(R.id.edtTDDesc)
        edtStarttime = findViewById(R.id.edtStarttime)
        spinnerTaskType = findViewById(R.id.spinnerTaskType)
        btnCreateTask = findViewById(R.id.btnCreateTask)

        val tdlSpinnerItems = arrayOf("Reminder", "Study", "Task")
        var adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tdlSpinnerItems)
        spinnerTaskType.adapter = adapter
        spinnerTaskType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Toast.makeText(applicationContext,"Selected type: ${tdlSpinnerItems[p2]}", Toast.LENGTH_SHORT).show()
                taskType=tdlSpinnerItems[p2].toLowerCase()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
        edtStarttime.setOnClickListener { view->
            getDTCalender()
            DatePickerDialog(this,this,year,month,day).show()
        }
        btnCreateTask.setOnClickListener {
            if (edtTDTitle.text.toString().length == 0) {
                var toast = Toast.makeText(this, "Please fill in title.", Toast.LENGTH_SHORT).show()
            } else if (edtTDDesc.text.toString().length == 0) {
                var toast =
                    Toast.makeText(this, "Please fill in description.", Toast.LENGTH_SHORT).show()
            } else if (edtStarttime.text.toString().length == 0) {
                var toast =
                    Toast.makeText(this, "Please fill in start time.", Toast.LENGTH_SHORT).show()
            } else if (taskType.equals("")){
                var toast =
                    Toast.makeText(this, "Please select a task type.", Toast.LENGTH_SHORT).show()
            }else{
                var id = randomGenerator()
                var URL = "http://${IPCONFIG.IP}/ProductivityApp/insertTaskData.php?email=${Person.email}&title=${edtTDTitle.text}&description=${edtTDDesc.text}&starttime=${edtStarttime.text}&type=${taskType}&id=$id"
                var requestQ = Volley.newRequestQueue(this)
                var stringRequest = StringRequest(Request.Method.GET, URL, Response.Listener { response->
                    var intent = Intent(this, ToDoList::class.java)
                    startActivity(intent)
                    var toast = Toast.makeText(this,"Successfully created task!",Toast.LENGTH_SHORT).show()
                }, Response.ErrorListener { error->
                    var toast = Toast.makeText(this,error.stackTraceToString(),Toast.LENGTH_SHORT).show()
                })
                requestQ.add(stringRequest)
            }

        }
    }
    var year = 0
    var month = 0
    var day = 0
    var hour = 0
    var minute = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedHour = 0
    var savedMinute = 0

    private fun randomGenerator():String{
        var IDcharacters = arrayListOf('1','2','3','4','5','6','7','8','9','0','a','b','c','d','e','f',
            'g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'
        )
        var id = ""
        for (i in 0.until(40)){
            val randomIndex = Random().nextInt(IDcharacters.size);
            id+= IDcharacters[randomIndex]
        }
        return id
    }
    private fun getDTCalender(){
        val myCalendar = Calendar.getInstance()
        year = myCalendar.get(Calendar.YEAR)
        month = myCalendar.get(Calendar.MONTH)
        day = myCalendar.get(Calendar.DAY_OF_MONTH)
        hour = myCalendar.get(Calendar.HOUR)
        minute = myCalendar.get(Calendar.MINUTE)
    }
    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        savedDay = p3
        savedMonth = p2
        savedYear = p1

        getDTCalender()
        TimePickerDialog(this,this,hour,minute,true).show()
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        var AMorPM = "AM"
        if (p1>12){
            savedHour=p1-12
            AMorPM="PM"
        }else{
            savedHour=p1
        }
        savedMinute= p2

        edtStarttime.text = "$savedMonth/$savedDay/$savedYear $savedHour:$savedMinute $AMorPM"
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean{
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        if (item?.itemId == R.id.pomodoro){
            var intent = Intent(this, Pomodoro::class.java)
            startActivity(intent)
        } else if (item?.itemId == R.id.todoList){
            var intent = Intent(this, ToDoList::class.java)
            startActivity(intent)
        }else if (item?.itemId == R.id.logout){
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }else if (item?.itemId == R.id.leaderboard){
            var intent = Intent(this, Leaderboard::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}