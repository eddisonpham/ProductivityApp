package com.example.productivityapp

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.startActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class ToDoListAdapter(var context: Context, var arrayList: ArrayList<TDLDATA>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inboxView = LayoutInflater.from(context).inflate(R.layout.tdl_row,parent,false)
        return InboxViewHolder(inboxView)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as InboxViewHolder).initializeRowUIComponent(
            arrayList.get(position).title,
            arrayList.get(position).starttime,
            arrayList.get(position).type,
            arrayList.get(position).description,
            arrayList.get(position).id
        )
    }
    inner class InboxViewHolder(iView: View): RecyclerView.ViewHolder(iView){
        var imgTask = iView.findViewById<ImageView>(R.id.imgTask)
        var tdlTitle = iView.findViewById<TextView>(R.id.tdlTitle)
        var tdlStarttime = iView.findViewById<TextView>(R.id.tdlStarttime)
        var tdlType = iView.findViewById<TextView>(R.id.tdlType)
        var taskRow = iView.findViewById<LinearLayout>(R.id.taskRow)
        fun initializeRowUIComponent(title:String, starttime:String, type:String, desc:String, id:String){
            tdlTitle.setText(title)
            tdlStarttime.setText(starttime)
            tdlType.setText(type.toUpperCase())
            var reminderImg = when(type){
                "study"->R.drawable.study
                "task"->R.drawable.tasks
                else ->R.drawable.reminder
            }
            if (type.equals("study")){
                taskRow.setBackgroundColor(getColor(context, R.color.purple))
                tdlTitle.setBackgroundColor(getColor(context, R.color.dark_purple))
                tdlTitle.setTextColor(getColor(context, R.color.light_purple))
                tdlStarttime.setBackgroundColor(getColor(context, R.color.dark_indigo))
                tdlType.setBackgroundColor(getColor(context, R.color.dark_indigo))
                tdlStarttime.setTextColor(getColor(context, R.color.light_purple))
                tdlType.setTextColor(getColor(context, R.color.light_purple))
            }else if (type.equals("task")){
                taskRow.setBackgroundColor(getColor(context, R.color.light_purple))
                tdlTitle.setBackgroundColor(getColor(context, R.color.dark_indigo))
                tdlStarttime.setBackgroundColor(getColor(context, R.color.purple))
                tdlType.setBackgroundColor(getColor(context, R.color.purple))
            }
            imgTask.setImageResource(reminderImg)
            taskRow.setOnClickListener {
                var intent = Intent(context, TDLTaskScreen::class.java)
                intent.putExtra("date",starttime)
                intent.putExtra("title", title)
                intent.putExtra("desc",desc)
                context.startActivity(intent)
            }
            taskRow.setOnLongClickListener {
                val dialogBuilder = AlertDialog.Builder(context)
                dialogBuilder.setTitle("Delete Task")
                dialogBuilder.setMessage("Are you sure you want to delete this task?")
                dialogBuilder.setNeutralButton("Yes",
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        var URL = "http://${IPCONFIG.IP}/ProductivityApp/deleteTDLTask.php?id=$id"
                        var requestQ = Volley.newRequestQueue(context)
                        var stringRequest = StringRequest(Request.Method.GET, URL, null,null)
                        requestQ.add(stringRequest)
                        var intent = Intent(context, ToDoList::class.java)
                        context.startActivity(intent)
                })
                dialogBuilder.setNegativeButton("No",null)
                dialogBuilder.create().show()
                true
            }
        }


    }
}