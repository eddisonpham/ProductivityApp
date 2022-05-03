package com.example.productivityapp

import android.app.Activity
import android.app.DialogFragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class PomodoroFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var fragmentView = inflater.inflate(R.layout.fragment_pomodoro, container, false)
        var txtLevel = fragmentView.findViewById<TextView>(R.id.txtLevel)
        var txtTasksComplete = fragmentView.findViewById<TextView>(R.id.txtTasksComplete)
        var levelUp = fragmentView.findViewById<TextView>(R.id.levelUp)
        Person.taskscompleted+=1
        if (!Person.level.equals("Omega")){
            var levelIndex = getLevelIndex(Person.level)
            var nextIndex = (levelIndex+1).toDouble()
            var totalReq = ((nextIndex/2)*(nextIndex+1)).toInt()
            if (totalReq==Person.taskscompleted){
                levelIndex+=1
                val templvl = Person.level
                Person.level=levelNames.arrayOfLevels[levelIndex]
                levelUp.setText("Level Up!")
                txtLevel.setText("$templvl -> ${Person.level}")
            }else{
                txtLevel.setText(Person.level)
            }
        }
        txtTasksComplete.setText("${Person.taskscompleted-1} -> ${Person.taskscompleted}")
        var URL = "http://${IPCONFIG.IP}/ProductivityApp/updateUserLevel.php?taskscompleted=${Person.taskscompleted}&level=${Person.level}&email=${Person.email}"
        var requestQ = Volley.newRequestQueue(activity)
        var stringRequest = StringRequest(Request.Method.GET, URL, null,null)
        requestQ.add(stringRequest)
        return fragmentView
    }
    fun getLevelIndex(level:String):Int{
        for (i in 0..levelNames.arrayOfLevels.size-1){
            if (levelNames.arrayOfLevels[i].equals(level)){
                return i
            }
        }
        return 0
    }
}