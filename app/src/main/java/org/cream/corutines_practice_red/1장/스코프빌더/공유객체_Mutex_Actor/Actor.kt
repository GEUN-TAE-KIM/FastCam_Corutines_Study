package org.cream.corutines_practice_red.`1장`.스코프빌더.공유객체_Mutex_Actor

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import org.cream.corutines_practice_red.massiveRun

/*
Note
 Actor = 독점적으로 자료를 가지며 그 자료를 다른 코루틴과 공유하지 않고 액터를 통해서만 접근 가능

TODO
 액터

me
 이런게 있다.
 */

sealed class CounterMsg // 자식이 정해진 클래스 확장 불가
object IncCounter : CounterMsg()
class GetCounter(val response: CompletableDeferred<Int>) : CounterMsg()

@OptIn(ObsoleteCoroutinesApi::class)
fun CoroutineScope.counterActor() = actor<CounterMsg> {

    var counter = 0 // 액터 안에 상태를 캡슐화해두고 다른 코루틴이 접근하지 못하게 합니다.

    for (msg in channel) { // 외부에서 보내는 것은 채널을 통해서만 받을 수 있습니다.(recieve)
        when (msg) {
            is IncCounter -> counter++ // 증가시키는 신호.
            is GetCounter -> msg.response.complete(counter) // 현재 상태를 반환합니다.
        }
    }

}

fun main() = runBlocking<Unit> {
    val counter = counterActor()
    withContext(Dispatchers.Default) {
        massiveRun {
            counter.send(IncCounter) // 값의 증가 요청
        }
    }

    val response = CompletableDeferred<Int>()
    counter.send(GetCounter(response)) // 값의 받는 요청
    println("Counter = ${response.await()}")
    counter.close()
}

