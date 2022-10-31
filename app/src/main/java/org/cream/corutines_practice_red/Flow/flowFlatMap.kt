package org.cream.corutines_practice_red.Flow

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

/*
Note
 flatMapConcat = 첫번째 요소에 대해서 플레트닝을 하고 나서 두번째 요소를 합니다.

TODO
 flatMapConcat

me
 첫번째 emit을 출력하고 기다리고 두번째 출력하고 다시 첫번째 출력하는 방식
 */
fun requestFlow(i: Int): Flow<String> = flow {
    emit("$i First")
    delay(1100)
    emit("$i: Second")
}

@OptIn(FlowPreview::class)
fun main23() = runBlocking {

    val startTime = System.currentTimeMillis()

    (1..3).asFlow().onEach { delay(100) }
        .flatMapConcat {
            requestFlow(it)
        }

        .collect { value ->
            println("$value at ${System.currentTimeMillis() - startTime} ms from start")
        }
}

/*
Note
 flatMapMerge = 첫 요소의 프레트닝을 시작하며 이어 다음 요소의 플레트닝을 시작합니다.

TODO
  flatMapMerge

me
 두번째 emit을 기다리면서 첫번쨰 emit을 딜레이동안 출력해야 할 것을 출력 하는 것
 */
@OptIn(FlowPreview::class)
fun main24() = runBlocking {

    val startTime = System.currentTimeMillis()

    (1..3).asFlow().onEach { delay(100) }
        .flatMapMerge {
            requestFlow(it)
        }

        .collect { value ->
            println("$value at ${System.currentTimeMillis() - startTime} ms from start")
        }
}

/*
Note
 flatMapLatest = 다음 요소의 플레트닝을 시작하며 이전에 진행 중이던 플레트닝을 취소합니다.

TODO
 flatMapLatest

me
 마지막을 출력 할 때는 다 출력이 되지만 그전에는 first를 출력하고 그 후는 캔슬을 해버려서 first만 출력이 됨
 */

@OptIn(ExperimentalCoroutinesApi::class)
fun main25() = runBlocking {

    val startTime = System.currentTimeMillis()

    (1..3).asFlow().onEach { delay(100) }
        .flatMapLatest {
            requestFlow(it)
        }

        .collect { value ->
            println("$value at ${System.currentTimeMillis() - startTime} ms from start")
        }
}