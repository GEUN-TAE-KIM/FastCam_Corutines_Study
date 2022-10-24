package org.cream.corutines_practice_red.`1장`.스코프빌더

import kotlinx.coroutines.*
import kotlin.IllegalArgumentException
import kotlin.random.Random
import kotlin.system.measureTimeMillis

/*
TODO
 suspend 함수들의 순차적인 수행

me
 대략 함수당 1초정도 딜레이를 갖고 랜덤으로 숫자 출력
 순차적으로 함수가 실행해서 2천밀리초의 시간이 걸린 것 각각 1000ms 소비
 */

suspend fun getRandom1(): Int {
    delay(1000L)
    return Random.nextInt(0, 500)
}

suspend fun getRandom2(): Int {
    delay(1000L)
    return Random.nextInt(0, 500)
}

fun main20() = runBlocking {

    val elapsedTime = measureTimeMillis {
        val value1 = getRandom1()
        val value2 = getRandom2()
        println("${value1} + ${value2} = ${value1 + value2}")
    }
    println(elapsedTime)

}

/*
Note
 async = 동시에 다른 블록을 수행
 await() = 수행된 결과를 가져옴
 둘이 세트

TODO
 async를 이용해 동시 수행
 결과를 받아야 한다면 async, 결과를 받지 않아도 된다면 launch를 선택할 수 있음

me
 await 키워드를 만나면 async 블록이 수행이 끝났는지 확인하고 아직 끝나지 않았다면 suspend되었다 나중에 다시 깨어나고 반환값을 받아옵니다.
 */

fun main21() = runBlocking {

    val elapsedTime = measureTimeMillis {

        val value1 = async {
            getRandom1()
        }
        val value2 = async {
            getRandom2()
        }

        //job.join() + 결과도 가져옴
        println("${value1.await()} + ${value2.await()} = ${value1.await() + value2.await()}")
    }
    println(elapsedTime)
}

/*
Note
 async(start = CoroutineStart.LAZY) = 인자로 전달하여 준비 상태가 됨
 start() = 수행 예약을 함

TODO
 aync 늦게 사용

 */
fun main22() = runBlocking {

    val elapsedTime = measureTimeMillis {

        val value1 = async(start = CoroutineStart.LAZY) {
            getRandom1()
        }


        val value2 = async(start = CoroutineStart.LAZY) {
            getRandom2()
        }

        value1.start()
        value2.start()

        //job.join() + 결과도 가져옴
        println("${value1.await()} + ${value2.await()} = ${value1.await() + value2.await()}")
    }
    println(elapsedTime)
}

/*
TODO
 async를 사용한 구조적인 동시성

me
 getRandom4의 예외가 발생하여 부모 코루틴은 자식 코루틴이 1개라도 문제가 생기면 예외를 발생시킴
 -> coroutineScope의 특징

 */
suspend fun getRandom3(): Int {
    try {
        delay(1000L)
        return Random.nextInt(0, 500)
    } finally {
        println("getRandom3 is cancelled")
    }
}

suspend fun getRandom4(): Int {
    delay(500L)
    throw IllegalArgumentException()
}

suspend fun doSomething() = coroutineScope {

    val value3 = async {
        getRandom3()
    }

    val value4 = async {
        getRandom4()
    }

    try {
        println("${value3.await()} + ${value4.await()} = ${value3.await() + value4.await()}")
    } finally {
        println("doSomething is cancelled.")
    }
}

fun main23() = runBlocking {
    try {
        doSomething()
    } catch (e: IllegalArgumentException) {
        println("doSomething failed: $e")
    }
}





