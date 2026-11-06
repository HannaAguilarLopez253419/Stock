package com.hann.shopu3.StationeryInventory

import android.app.Application
import com.hann.shopu3.StationeryInventory.di.AppContainer
import kotlin.getValue

class ShopApplication : Application() {
    val container: AppContainer by lazy { AppContainer(applicationContext) }
}