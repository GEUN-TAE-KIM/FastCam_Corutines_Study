package org.cream.corutines_practice_red.Flow

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

/*
TODO
 명령형 finally 블록
 */
fun simple7(): Flow<Int> = (1..3).asFlow()

fun main30() = runBlocking {
    try {
        simple7().collect { value -> println(value) }
    } finally {
        println("Done")
    }
}

/*
Note
 onCompletion 연산자를 선언해서 완료를 처리할 수 있음

TODO
 선언적으로 완료 처리하기
 */
fun main31() = runBlocking {
    simple7()
        .onCompletion { println("Done") }
        .collect { value -> println("$value") }
}

/*
Note
 onCompletion은 종료 처리를 할 때 예외가 발생되었는지 여부를 알 수 있음

TODO
 onCompletion의 장점

me
 try-catch 후 finally에서는 문제를 알수 없지만 onCompletion은 알수가 있음
 */
fun main32() = runBlocking {
    simple7()
        .onCompletion { cause ->
            if (cause != null) {
                println("Flow completed exceptionally")
            } else {
                println("Flow completed")
            }
        }

        .catch { cause ->
            println("Caught exception")
        }

        .collect { value ->
            println(value)
        }
}