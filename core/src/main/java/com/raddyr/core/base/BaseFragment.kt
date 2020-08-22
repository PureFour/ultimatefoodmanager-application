package com.raddyr.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.raddyr.core.ui.dialogManager.DialogManager
import javax.inject.Inject

abstract class BaseFragment : Fragment() {

    @Inject
    lateinit var dialogManager: DialogManager

    protected abstract val contentViewLayout: Int

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterView()
    }

    protected abstract fun afterView()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(contentViewLayout, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as BaseActivity).subscriptionManager.unregisterAll()
        dialogManager.hideProgressBar()
    }
}