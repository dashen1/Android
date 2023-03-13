package com.example.kotlin.utils

object EspressoIdlingResource {

    private const val RESOURCE = "GLOBAL"

    // @JvmField 告诉编译器不要生成 getter()和setter()方法，也就是说不对外暴露
    @JvmField
    val countingIdlingResource = SimpleCountingIdlingResource(RESOURCE)

    fun increment(){
        countingIdlingResource.increment();
    }

    fun decrement(){
        if (!countingIdlingResource.isIdleNow){
            countingIdlingResource.decrement()
        }
    }
}

inline fun <T> wrapEspressoIdlingResource(function: ()->T):T{
    // Espresso does not work well with coroutines yet.
    EspressoIdlingResource.increment() // Set app as busy.
    return try {
        function()
    } finally {
        EspressoIdlingResource.decrement() // Set app as idle.
    }
}