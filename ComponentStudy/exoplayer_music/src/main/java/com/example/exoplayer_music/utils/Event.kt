package com.example.exoplayer_music.utils

class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set

    /**
     * Return the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */

    fun peekContent(): T = content
}