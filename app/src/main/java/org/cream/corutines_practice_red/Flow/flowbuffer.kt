package org.cream.corutines_practice_red.Flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

/*
TODO
 buffer

me
 buffer가 없는 경우에는 양측 다 시간을 기다리게 됨
 */
fun simple4(): Flow<Int> = flow {
    for (i in 1..10) {
        delay(100)
        emit(i)
    }
}

fun main18() = runBlocking {
    val time = measureTimeMillis {
        simple4().buffer()
            .collect { value ->
                delay(300)
                println(value)
            }
    }
    println("Collected in $time ms")
}

/*
Note
 conflate = 이용하면 중간의 값을 융합(conflate)할 수 있음, 처리보다 빨리 발생한 데이터의 중간 값들을 누락

TODO
 conflate
 */
fun main19() = runBlocking {
    val time = measureTimeMillis {
        simple4().conflate()
            .collect { value ->
                delay(300)
                println(value)
            }
    }
    println("Collected in $time ms")
}

/*
Note
 conflate = 수집 측이 느릴 경우 새로운 데이터가 있을 때 수집 측을 종료시키고 새로 시작하는 방법

TODO
 마지막 값만 처리

me
 마지막 값만 출력을 하는 것
 */
fun main20() = runBlocking {
    val time = measureTimeMillis {
        simple4()
            .collectLatest { value -> // 1, 2 // 리셋 2,3 // 리셋 3
                println("값 ${value}를 처리하기시작")
                delay(300)
                println("값 출력 : $value")
                println("처리 완료")
            }
    }
    println("Collected in $time ms")
}