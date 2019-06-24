package com.rhtyme.eventmanager

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.ColorInt
import android.text.Html
import android.view.View
import android.widget.TextView
import com.rhtyme.eventmanager.events.AnimalEvent
import com.rhtyme.eventmanager.events.ColorEvent
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity() {


    private var activityResume: Boolean = false

    var colorEventHistory: String = ""

    var animalEventHistory: String = ""

    lateinit var eventManager: EventManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        eventManager = EventManager()
        EventBus.getDefault().register(this)
        startService(Intent(this@MainActivity, CounterService::class.java))
    }

    override fun onResume() {
        super.onResume()
        activityResume = true
        eventManager.activityResumed()
    }

    override fun onPause() {
        super.onPause()
        activityResume = false
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    fun onColorEvent(colorEvent: ColorEvent) {
        if (!colorEvent.fromCache) {
            colorEventHistory += makeUpString(colorEvent.colorName, colorEvent.colorCode) + ", "
        }

        if (!handleEvent(colorEvent)) {
            return
        }

        GlobalScope.launch(Dispatchers.Main) {
            setupHtmlText(colorEventHistory, tvEventColorsHistory)
            tvEventColors.text = colorEvent.colorName
            tvEventColors.setTextColor(colorEvent.colorCode)
        }

    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    fun onAnimalEvent(animalEvent: AnimalEvent) {
        if (!animalEvent.fromCache) {
            animalEventHistory += makeUpString(animalEvent.animalName, animalEvent.colorCode) + ", "
        }

        if (!handleEvent(animalEvent)) {
            return
        }

        GlobalScope.launch(Dispatchers.Main) {
            setupHtmlText(animalEventHistory, tvEventAnimalsHistory)
            tvEventAnimals.text = animalEvent.animalName
            tvEventAnimals.setTextColor(animalEvent.colorCode)
        }

    }

    private fun handleEvent(event: Any): Boolean {
        if (activityResume) {
            return true
        }
        eventManager.addEvent(event)
        return false
    }


    fun repeat(view: View) {
        clear(tvEventColors).clear(tvEventAnimals).clear(tvEventColorsHistory).clear(tvEventAnimalsHistory)
        colorEventHistory = ""
        animalEventHistory = ""
        startService(Intent(this@MainActivity, CounterService::class.java))
    }

    fun nextActivity(view: View) {
        startActivity(Intent(this@MainActivity, SecondActivity::class.java))
    }

        fun setupHtmlText(htmlText: String, textView: TextView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.text = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
        } else {
            textView.text = Html.fromHtml(htmlText)
        }
    }

    fun makeUpString(text: String, @ColorInt color: Int): String {
        val sb = StringBuilder()
        sb.append("<font color=\"").append(color).append("\">").append(text).append("</font>")
        return sb.toString()
    }

    fun clear(textView: TextView): MainActivity {
        textView.text = ""
        return this@MainActivity
    }
}
