package com.raddyr.core.util.logout

import android.content.Context
import com.raddyr.core.data.rx.Subscribers

interface LogoutManager {
    fun logout(context: Context, subscriptionManager: Subscribers.Companion.SubscriptionManager)
}