package org.cream.corutines_practice_red.Flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

/*
Note
 zip = 양쪽의 데이터를 한꺼번에 묶어 새로운 데이터를 만들어 냄

TODO
 zip으로 묶기

me
 .onEach { delay(200L) } 해도 적용이 안됨
 */
fun main() = runBlocking {
    val nums = (1..3).asFlow()
    val strs = flowOf("일", "이", "삼")
    nums.zip(strs) { a, b -> "${a}는 $b" }
        .collect {
            println(it)
        }
}

/*
Note
 combine = 양쪽의 데이터를 같은 시점에 묶지 않고 한 쪽이 갱신되면 새로 묶어 데이터를 만듬

TODO
 combine으로 묶기
 */
fun main22() = runBlocking {
    val nums = (1..3).asFlow().onEach { delay(100L) }
    val strs = flowOf("일", "이", "삼").onEach { delay(200L) }
    nums.combine(strs) { a, b -> "${a}는 $b" }
        .collect {
            println(it)
        }
}