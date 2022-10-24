package org.cream.corutines_practice_red

import kotlinx.coroutines.*
import kotlin.random.Random

/*
Note
 Coroutine Exception Handler = 예외 처리 및 예외 취소

TODO
 Coroutine Exception Handler

me
 일반적인 try catch 등 보다 통합적인 관리에 유리
 */
suspend fun printRandom1() {
    delay(1000L)
    println(Random.nextInt(0, 500))
}

suspend fun printRandom2() {
    delay(500L)
    throw ArithmeticException()
}

val ceh = CoroutineExceptionHandler { _, exception ->
    println("Something happened: $exception")
}

fun main32() = runBlocking {

    val scope = CoroutineScope(Dispatchers.IO)

    val job = scope.launch(ceh) {

        launch {
            printRandom1()
        }

        launch {
            printRandom2()
        }
    }
    job.join()

}

/*
Note
 runBlocking에서는 CEH를 사용할 수 없음 runBlocking은 자식이 예외로 종료되면 항상 종료되고 CEH를 호출하지 않음

TODO
 runBlocking과 Coroutine Exception Handler
 */
suspend fun getRandom1(): Int {
    delay(1000L)
    return Random.nextInt(0, 500)
}

suspend fun getRandom2(): Int {
    delay(500L)
    throw ArithmeticException()
}

fun main33() = runBlocking {

    val job = launch(ceh) {

        val a = async { getRandom1() }
        val b = async { getRandom2() }
        println(a.await())
        println(b.await())

    }
    job.join()
}

/*
Note
 SupervisorJob = 예외에 의한 취소를 아래쪽으로 내려가게 함

TODO
 SupervisorJob

me
 joinAll = 변수.join 한걸 한번에 하는 것
 */
fun main34() = runBlocking {
    val scope = CoroutineScope(Dispatchers.IO + SupervisorJob() + ceh)

    val job1 = scope.launch {
        printRandom1()
    }

    val job2 = scope.launch {
        printRandom2()
    }
    joinAll(job1, job2)
}

/*
Note
 SupervisorScope = 코루틴 스코프와 슈퍼바이저 잡을 합침
  -> supervisorScope에는 아래 CoroutineExceptionHandler를 꼭 해줘야함 or try catch

TODO
 SupervisorScope

me
 사용할 때 주의점은 무조건 자식 수준에서 예외를 핸들링 해야한다는 것
 자식의 실패가 부모에게 전달되지 않기 때문에 자식 수준에서 예외를 처리해야합니다.
 */
suspend fun supervisorFunc() = supervisorScope {
    launch { printRandom1() }
    launch(ceh) { printRandom2() }
}

fun main() = runBlocking {
    val scope = CoroutineScope(Dispatchers.IO)
    val job = scope.launch {
        supervisorFunc()
    }
    job.join()
}
