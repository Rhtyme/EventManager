package com.rhtyme.eventmanager

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.rhtyme.eventmanager.events.AnimalEvent
import com.rhtyme.eventmanager.events.ColorEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import kotlin.random.Random
import kotlin.random.nextInt


class CounterService : Service() {

    private var counterAnimal = 15

    private var counterColor = 15

    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        publishAnimals()

        publishColors()

        return super.onStartCommand(intent, flags, startId)
    }

    private fun publishColors() {
        val rainbow = resources.getIntArray(R.array.rainbow)

        val rainbowTitle = resources.getStringArray(R.array.rainbow_title)

        GlobalScope.launch(Dispatchers.IO) {

            for (i in 0 until rainbow.size) {
                val event = ColorEvent(rainbow[i], rainbowTitle[i])
                EventBus.getDefault().post(event)
                delay(2000)
                // Do something with the paint.
            }
        }
    }

    private fun publishAnimals() {
        val rainbow = resources.getIntArray(R.array.rainbow)

        val animals = resources.getStringArray(R.array.animals_array)

        GlobalScope.launch(Dispatchers.IO) {

            for (i in 0 until rainbow.size) {
                val event = AnimalEvent(rainbow.random(), animals[i])
                EventBus.getDefault().post(event)
                delay(2000)
                // Do something with the paint.
            }
        }
    }
}
