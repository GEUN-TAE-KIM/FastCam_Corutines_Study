package org.cream.corutines_practice_red

import kotlinx.coroutines.*
import kotlin.random.Random

/*
Note
 GlobalScope = 어디에도 속하지 않고 원래부터 존재하는 전역 스코프

TODO
 GlobalScope

me
 1. sleep을 쓸 수 밖에 없는게 메인이 기본 함수라서 delay를 쓸수가 없음
 2. GlobalScope는 어떤 계층에도 속하지 않고 영원히 동작하게 된다는 문제점이 있어서 안쓰인다고 함
    -> 이런게 있다.
 */
suspend fun printRandom() {
    delay(500L)
    println(Random.nextInt(0, 500))
}

/*
= 코틀린 코루틴은 구조화된 동시성을 따르기 때문에 이것을 쓰는 것은
GlobalScope가 코루틴을 완료할 수 없는 경우
계속 실행하고 시스템 리소스를 소비하여 메모리 누수가 발생 할 수 있기 때문이라고 함
*/
@OptIn(DelicateCoroutinesApi::class)
fun main30() {

    val job = GlobalScope.launch(Dispatchers.IO) {
        launch {
            printRandom()
        }
    }
    Thread.sleep(1000L)
}

/*
Note
 CoroutineScope = 인자를 CoroutineContext를 받는데 엘리먼트를 하나 or 합쳐서 만듬
 -계층적으로 형성된 코루틴을 관리

TODO
 CoroutineScope
 */
fun main31() {

    val scope = CoroutineScope(Dispatchers.Default)

    val job = scope.launch(Dispatchers.IO) {
        launch {
            printRandom()
        }
    }
    Thread.sleep(1000L)
}










