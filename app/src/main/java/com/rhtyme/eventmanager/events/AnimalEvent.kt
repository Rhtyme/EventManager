package com.rhtyme.eventmanager.events

data class AnimalEvent(var colorCode: Int = 0, var animalName: String = "", var fromCache: Boolean = false)