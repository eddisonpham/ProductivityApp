package com.example.productivityapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

class Pomodoro : AppCompatActivity() {
    private lateinit var periodTitle: TextView
    private lateinit var spinnerHand: ImageView
    private lateinit var pomoTimer: TextView
    private lateinit var btnStartPomo: Button

    private var pomoSchedule = arrayListOf<Int>(1500,300,1500,300,1500,300,1500,300)
    private var index = 0
    var context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pomodoro)
        periodTitle=findViewById(R.id.periodTitle)
        spinnerHand=findViewById(R.id.spinnerHand)
        pomoTimer=findViewById(R.id.pomoTimer)
        btnStartPomo=findViewById(R.id.btnStartPomo)
        btnStartPomo.setOnClickListener {
            btnStartPomo.isVisible=false
            btnStartPomo.isEnabled=false
            val spinanim = AnimationUtils.loadAnimation(this, R.anim.spinanim)
            spinnerHand.startAnimation(spinanim)
            var countDownTime = pomoSchedule[index]
            if (countDownTime==1500){
                periodTitle.setText("STUDY")
                periodTitle.setTextColor(ContextCompat.getColor(this, R.color.dark_purple))
                periodTitle.setBackgroundColor(ContextCompat.getColor(this, R.color.light_purple))
            }else{
                periodTitle.setText("BREAK")
                periodTitle.setTextColor(ContextCompat.getColor(this, R.color.light_purple))
                periodTitle.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_purple))
            }
            val timer = object: CountDownTimer((pomoSchedule[index]*1000).toLong(), 1000.toLong()) {
                override fun onTick(millisUntilFinished: Long) {
                    countDownTime-=1
                    var minutes = countDownTime.floorDiv(60)
                    var seconds = countDownTime.mod(60)
                    var s = seconds.toString()
                    if (seconds<10){
                        s="0"+s
                    }
                    pomoTimer.setText("$minutes:$s")
                }
                override fun onFinish() {
                    btnStartPomo.isVisible=true
                    btnStartPomo.isEnabled=true
                    var m:Int
                    if (index == pomoSchedule.size-1){
                        m = if (index.mod(2)==0)25 else 5
                        index = 0
                        periodTitle.setText("POMODORO")
                        periodTitle.setTextColor(ContextCompat.getColor(context, R.color.light_purple))
                        periodTitle.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_purple))
                        var fragment = PomodoroFragment()
                        var fragmentManager = context.fragmentManager
                        fragment.show(fragmentManager,"Tag")
                    }else{
                        index+=1
                        m = if (index.mod(2)==0)25 else 5
                        if (m==25){
                            periodTitle.setText("STUDY")
                            periodTitle.setTextColor(ContextCompat.getColor(context, R.color.dark_purple))
                            periodTitle.setBackgroundColor(ContextCompat.getColor(context, R.color.light_purple))
                        }else{
                            periodTitle.setText("BREAK")
                            periodTitle.setTextColor(ContextCompat.getColor(context, R.color.light_purple))
                            periodTitle.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_purple))
                        }
                    }
                    spinnerHand.clearAnimation()
                    pomoTimer.setText("$m:00")
                }
            }
            timer.start()
        }
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