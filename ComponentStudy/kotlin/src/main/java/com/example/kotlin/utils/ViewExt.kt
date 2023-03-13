package com.example.kotlin.utils

import android.app.Service
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.google.android.material.snackbar.Snackbar

fun View.toVisible() {
    this.visibility = View.VISIBLE
}

fun View.toGone() {
    this.visibility = View.GONE
}

fun View.showKeyboard() {
    (this.context.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.showSoftInput(this, 0)
}

fun View.hideKeyBoard() {
    (this.context.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(
        this.windowToken,
        0
    )
}

fun View.showToast(
    lifecycleOwner: LifecycleOwner,
    toastEvent: LiveData<SingleEvent<Any>>,
    timeLength: Int
) {
    toastEvent.observe(lifecycleOwner) { event ->
        event.getContentIfNotHandled()?.let {
            when (it) {
                is String -> Toast.makeText(this.context, it, timeLength).show()
                is Int -> Toast.makeText(this.context, this.context.getString(it), timeLength)
                    .show()
                else -> {}
            }
        }
    }
}

fun View.showSnackBar(snackbarText:String,timeLength: Int){
    Snackbar.make(this, snackbarText,timeLength).run {
        addCallback(object : Snackbar.Callback(){
            override fun onShown(sb: Snackbar?) {
                EspressoIdlingResource.increment()
            }

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                EspressoIdlingResource.decrement()
            }
        })
        show()
    }
}

fun View.setupSnackBar(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<SingleEvent<Any>>,
    timeLength: Int
) {
    snackbarEvent.observe(lifecycleOwner) { event ->
        event.getContentIfNotHandled()?.let {
            when (it) {
                is String -> {
                    hideKeyBoard()
                    showSnackBar(it, timeLength)
                }
                is Int -> {
                    hideKeyBoard()
                    showSnackBar(this.context.getString(it), timeLength)
                }
                else -> {

                }
            }
        }
    }
}