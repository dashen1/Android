package com.example.kotlin

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


suspend fun simple(): List<Int> {
        delay(3000)
        return listOf(1, 2, 3)
    }

fun simple2(): Flow<Int> = flow{
    for (i in 1..3){
        delay(3000)
        emit(i)
    }
}


fun main(args: Array<String>) = runBlocking {
    // simple().forEach { value -> println(value) }
    GlobalScope.launch {
        simple2().collect { value -> println(value) }
    }

    println("hello world")
    Thread.sleep(8000)

}
//    companion object{
//        @JvmStatic
//        fun main(array: Array<String>){
//            var test = Test()
//            test.simple().forEach { value -> println(value) }
//            println("hello I am main function!")
//        }


