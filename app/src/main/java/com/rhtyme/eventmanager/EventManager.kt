package com.rhtyme.eventmanager

import com.rhtyme.eventmanager.events.AnimalEvent
import com.rhtyme.eventmanager.events.ColorEvent
import org.greenrobot.eventbus.EventBus

/**
 * Manager that re-sends events on activity resume, which are received when activity was stopped but was not destroyed yet.
 */
class EventManager {

    private val eventContainer = ArrayList<Any>()

    /**
     * method that adds an event to the event list avoiding duplicates/unnecessary events
     */
    @Synchronized
    fun addEvent(event: Any) {

        if (event is AnimalEvent) {
            addAnimalEvent(event)
            return
        }

        if (event is ColorEvent) {
            addColorEvent(event)
        }
    }

    private fun addColorEvent(event: ColorEvent) {
        for (cachedOne in eventContainer) {
            if (cachedOne is ColorEvent) {
                cachedOne.colorCode = event.colorCode
                cachedOne.colorName = event.colorName
                cachedOne.fromCache = true
                return
            }
        }
        eventContainer.add(event)
    }

    private fun addAnimalEvent(event: AnimalEvent) {
        for (cachedOne in eventContainer) {
            if (cachedOne is AnimalEvent) {
                cachedOne.colorCode = event.colorCode
                cachedOne.animalName = event.animalName
                cachedOne.fromCache = true
                return
            }
        }
        eventContainer.add(event)
    }

    @Synchronized
    fun activityResumed() {
        for (event in eventContainer) {
            EventBus.getDefault().post(event)
        }
    }

}