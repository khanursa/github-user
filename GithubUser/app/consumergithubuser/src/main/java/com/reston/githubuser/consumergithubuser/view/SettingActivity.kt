package com.reston.githubuser.consumergithubuser.view

import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import com.reston.githubuser.consumergithubuser.R
import com.reston.githubuser.consumergithubuser.service.AlarmReciver
import kotlinx.android.synthetic.main.activity_setting_githubuser.*

class SettingActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {
    private val alarm by lazy { AlarmReciver() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_githubuser)

        if (alarm.isAlarmSet(this@SettingActivity)) {
            switchs.isChecked = true
            label_alarm.text = "Hidupkan Alarm"
        } else {
            switchs.isChecked = false
            label_alarm.text = "Matikan Alarm"
        }
        switchs.setOnCheckedChangeListener(this)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (isChecked) {
            label_alarm.text = "Matikan Alarm"
            alarm.setRepeatingAlarm(this@SettingActivity)
        } else {
            label_alarm.text = "Hidupkan Alarm"
            alarm.cancelAlarm(this@SettingActivity)
        }
    }
}