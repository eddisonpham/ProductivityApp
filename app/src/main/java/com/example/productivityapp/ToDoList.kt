package com.example.productivityapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ToDoList : AppCompatActivity() {
    private lateinit var tdlRV:RecyclerView
    private lateinit var openActionButtons:FloatingActionButton
    private lateinit var selectAll:FloatingActionButton
    private lateinit var selectTasks:FloatingActionButton
    private lateinit var selectReminder:FloatingActionButton
    private lateinit var selectStudy:FloatingActionButton
    private lateinit var addNewTask:FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do_list)
        tdlRV=findViewById(R.id.tdlRV)
        openActionButtons=findViewById(R.id.openActionButtons)
        selectAll=findViewById(R.id.selectAll)
        selectTasks=findViewById(R.id.selectTasks)
        selectReminder=findViewById(R.id.selectReminder)
        selectStudy=findViewById(R.id.selectStudy)
        addNewTask=findViewById(R.id.addNewTask)
        something("all")
        addNewTask.setOnClickListener{
            var intent = Intent(this, MakeNewTask::class.java)
            startActivity(intent)
        }
        selectStudy.setOnClickListener{
            something("study")
        }
        selectReminder.setOnClickListener{
            something("reminder")
        }
        selectTasks.setOnClickListener{
            something("task")
        }
        selectAll.setOnClickListener{
            something("all")
        }
        var visible = false
        openActionButtons.setOnClickListener{
            visible = !visible
            selectAll.isVisible=visible
            selectTasks.isVisible=visible
            selectReminder.isVisible=visible
            selectStudy.isVisible=visible
            addNewTask.isVisible=visible

            selectAll.isEnabled=visible
            selectTasks.isEnabled=visible
            selectReminder.isEnabled=visible
            selectStudy.isEnabled=visible
            addNewTask.isEnabled=visible
            
            if(visible){
                openActionButtons.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24)
            }else{
                openActionButtons.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24)
            }
        }
    }
    fun something(filter:String){
        var arrayOfData = ArrayList<TDLDATA>()
        var URL = "http://${IPCONFIG.IP}/ProductivityApp/getTDLDataWithEmail.php?email=${Person.email}"
        var requestQ = Volley.newRequestQueue(this)
        var jsonAR = JsonArrayRequest(Request.Method.GET, URL , null, Response.Listener { response->
            for (index in 0.until(response.length())){
                var json = response.getJSONObject(index)
                if (filter.equals(json.getString("type"))||filter.equals("all")){
                    arrayOfData.add(
                        TDLDATA(
                            json.getString("email"),
                            json.getString("title"),
                            json.getString("description"),
                            json.getString("starttime"),
                            json.getString("type"),
                            json.getString("id")
                        )
                    )
                }
            }
            var msList = ArrayList<Int>()
            for (i in 0..arrayOfData.size-1){
                msList.add(dateToInteger(arrayOfData[i].starttime.split(" ")))
            }
            for (i in msList){
                Log.i("tag",i.toString())
            }
            for (i in 0..msList.size-2){
                var min = i
                for (j in (i+1)..msList.size-1){
                    if (msList[j]<msList[min]){
                        min = j
                    }
                }
                var temp = msList[min]
                msList[min] = msList[i]
                msList[i] = temp

                var temp2 = arrayOfData[min]
                arrayOfData[min] = arrayOfData[i]
                arrayOfData[i] = temp2
            }
            var adapter = ToDoListAdapter(this, arrayOfData)
            tdlRV.layoutManager=LinearLayoutManager(this)
            tdlRV.adapter = adapter
        }, Response.ErrorListener { error->
            var toast = Toast.makeText(this, error.stackTraceToString(), Toast.LENGTH_SHORT).show()
        })
        requestQ.add(jsonAR)
    }
    fun dateToInteger(strings:List<String>):Int{
        var DTnumber = 0
        if (strings[2].equals("PM")){
            DTnumber+=12*60
        }
        var timeSplit = strings[1].split(":")
        DTnumber+=(timeSplit[0].toInt()*60)+(timeSplit[0].toInt())
        var dateList = strings[0].split("/")
        var msDate = minutes("${dateList[2]}-${dateList[0]}-${dateList[1]}")
        return msDate+DTnumber
    }
    fun minutes (date:String):Int{
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val mDate: Date = sdf.parse(date)
        val timeInSeconds: Double = mDate.getTime().toDouble()
        return (timeInSeconds/60000).toInt()
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