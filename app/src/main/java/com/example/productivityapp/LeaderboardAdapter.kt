package com.example.productivityapp

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class LeaderboardAdapter(var context: Context, var arrayList: ArrayList<LeaderboardPerson>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inboxView = LayoutInflater.from(context).inflate(R.layout.leaderboard_row,parent,false)
        return InboxViewHolder(inboxView)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as InboxViewHolder).initializeRowUIComponent(
            arrayList.get(position).name,
            arrayList.get(position).level,
            arrayList.get(position).taskscompleted,
            "#${position+1}"
        )
    }
    inner class InboxViewHolder(iView: View): RecyclerView.ViewHolder(iView){
        var LBnumber = iView.findViewById<TextView>(R.id.LBnumber)
        var LBName = iView.findViewById<TextView>(R.id.LBName)
        var LBLevel = iView.findViewById<TextView>(R.id.LBLevel)
        var LBTasksCompleted = iView.findViewById<TextView>(R.id.LBTasksCompleted)
        var LBLayout = iView.findViewById<LinearLayout>(R.id.LBLayout)
        fun initializeRowUIComponent(name:String, level:String, taskscompleted:String, rank:String){
            var factorOf12 = (48-((rank.length-2)*12)).toFloat()
            LBnumber.setTextSize(TypedValue.COMPLEX_UNIT_SP,factorOf12)
            LBnumber.setText(rank)
            LBName.setText(name)
            LBLevel.setText("Level: ${level}")
            LBTasksCompleted.setText("Tasks Completed: ${taskscompleted}")
            LBLayout.setOnClickListener {
            }
        }


    }
}