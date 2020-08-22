package com.raddyr.core.util.interfaces

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner

interface SharedInfoService {

    fun fetchData(activity: AppCompatActivity, callback: () -> Unit)
}