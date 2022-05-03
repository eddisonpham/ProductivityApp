package com.example.productivityapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

class Leaderboard : AppCompatActivity() {
    private lateinit var rvLeaderboard: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)
        rvLeaderboard=findViewById(R.id.rvLeaderboard)
        var usersArray = ArrayList<LeaderboardPerson>()
        var URL = "http://${IPCONFIG.IP}/ProductivityApp/getAllUsersTaskCompleted.php"
        var requestQ = Volley.newRequestQueue(this)
        var jsonAR = JsonArrayRequest(Request.Method.GET,URL,null,Response.Listener { response->
            for (jsonObject in 0.until(response.length())){
                var json = response.getJSONObject(jsonObject)
                usersArray.add(
                    LeaderboardPerson(
                        json.getString("firstname")+" "+json.getString("lastname"),
                        json.getString("taskscompleted"),
                        json.getString("level")
                    )
                )
            }
            var tasksCompleteArray = ArrayList<Int>()
            for (i in usersArray){
                tasksCompleteArray.add(i.taskscompleted.toInt())
            }
            for (i in 0..tasksCompleteArray.size-2){
                var max = i
                for (j in (i+1)..tasksCompleteArray.size-1){
                    if (tasksCompleteArray[j]>tasksCompleteArray[max]){
                        max = j
                    }
                }
                var temp = tasksCompleteArray[max]
                tasksCompleteArray[max] = tasksCompleteArray[i]
                tasksCompleteArray[i] = temp

                var temp2 = usersArray[max]
                usersArray[max] = usersArray[i]
                usersArray[i] = temp2
            }
            var adapter = LeaderboardAdapter(this, usersArray)
            rvLeaderboard.layoutManager= LinearLayoutManager(this)
            rvLeaderboard.adapter = adapter
        }, Response.ErrorListener { error->

        })
        requestQ.add(jsonAR)

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