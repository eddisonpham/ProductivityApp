package com.example.productivityapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView

class TDLTaskScreen : AppCompatActivity() {
    private lateinit var tvDate: TextView
    private lateinit var tvTitle: TextView
    private lateinit var tvDesc: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tdltask_screen)
        tvDate=findViewById(R.id.tvDate)
        tvTitle=findViewById(R.id.tvTitle)
        tvDesc=findViewById(R.id.tvDesc)

        var date = intent.getStringExtra("date").toString()
        var title = intent.getStringExtra("title").toString()
        var desc = intent.getStringExtra("desc").toString()
        tvDate.setText(date)
        tvTitle.setText(title)
        tvDesc.setText(desc)
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