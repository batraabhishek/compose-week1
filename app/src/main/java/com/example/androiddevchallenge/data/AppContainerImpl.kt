package com.example.androiddevchallenge.data

import android.content.Context
import com.example.androiddevchallenge.data.pupies.PuppiesRepository
import com.example.androiddevchallenge.data.pupies.impl.BlockingFakePuppiesRepository


/**
 * Dependency Injection container at the application level.
 */
interface AppContainer {
    val puppiesRepository: PuppiesRepository
}

class AppContainerImpl(private val applicationContext: Context) : AppContainer {
    override val puppiesRepository: PuppiesRepository by lazy {
        BlockingFakePuppiesRepository(context = applicationContext)
    }

}