package org.cream.corutines_practice_red.Flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

/*
Note
 addEventListener 대신 플로우의 onEach를 사용할 수 있음 이벤트마다 onEach가 대응하는 것입니다.

TODO
 이벤트를 Flow로 처리하기

me
 하지만 collect는 스트림이 끝날때가지 기다림 그래서 이벤트 처리에는
 -> ※ 적합하지 않음
 */
fun events(): Flow<Int> = (1..3).asFlow().onEach { delay(100) }

fun main33() = runBlocking {
    events()
        .onEach { events -> println("Event: $events") }
        .collect() // 여러 이벤트에 사용 불가 ui,네트워크 호출 등등
    println("Done")
}

/*
Note
 launchIn =별도의 코루틴에서 플로우를 런칭할 수 있음

TODO
 launchIn을 사용하여 런칭하기
*/
fun main34() = runBlocking {
    events()
        .onEach { events -> println("Event: $events") }
        .launchIn(this) // 새로운 코루틴을 만듬
    println("Done")
    // 여러 이벤트에 호출 가능능
}

fun main35() {
    val test = "宅配ボックス\n部屋番号：？？？？"
    println(test)

}
