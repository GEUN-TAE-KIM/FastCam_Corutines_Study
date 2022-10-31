package org.cream.corutines_practice_red.Flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

/*
Note
 map = 키, 값으로 나타내며 원하는 형태로 변환
       새로 만들어지는 컬렉션은 원본 컬렉션과 원소의 개수는 같지만 각 원소는 주어진 람다(함수)에 따라 변환된다.

TODO
 플로우와 map
 */
fun main5() = runBlocking {

    flowSomething().map {
        "$it $it"
    }.collect { value ->
        println(value)
    }

}

/*
Note
 filter =  true를 반환하는(조건에 맞는) 원소만 필터링하는 기능,
           주어진 조건문에 만족하는 원소만으로 이루어진 새로운 컬렉션

TODO
 플로우와 filter
 */
fun main6() = runBlocking {
    (1..20).asFlow().filter {
        (it % 2) == 0
    }.collect {
        println(it)
    }

}

/*
TODO
 filterNot
 */
fun main7() = runBlocking {
    (1..20).asFlow().filterNot {
        (it % 2) == 0
    }.collect {
        println(it)
    }
}

/*
Note
 transform 연산자를 이용해 조금 더 유연하게 스트림을 변형할 수 있음

TODO
 transform 연산자
 */
suspend fun someCalc(i: Int): Int {
    delay(10L)
    return i * 2
}

fun main8() = runBlocking {
    (1..20).asFlow().transform {
        emit(it) // 1 (delay) 2 3
        emit(someCalc(it)) // 2 4 6
    }.collect {
        println(it) // 1 2 2 4 3 6
    }
}

/*
Note
 take 연산자는 몇개의 수행 결과만을 정해서 출력

TODO
 take 연산자
 */
fun main9() = runBlocking {
    (1..20).asFlow().transform {
        emit(it)
        emit(someCalc(it))
    }.take(5)
        .collect {
            println(it)
        }
}

/*
Note
 takeWhile = 조건을 만족하는 동안만 값을 가져와서 출력

TODO
 takeWhile 연산자
 */
fun main10() = runBlocking {
    (1..20).asFlow().transform {
        emit(it)
        emit(someCalc(it))
    }.takeWhile {
        it >= 1
    }.collect {
        println(it)
    }
}

/*
Note
 drop = 처음 몇개의 결과를 버림, take가 takeWhile을 가지듯 dropWhile도 있음

TODO
 drop 연산자
 */
fun main11() = runBlocking {
    (1..20).asFlow().transform {
        emit(it)
        emit(someCalc(it))
    }/*.drop(5)
        .collect {
            println(it)
        }*/
        .dropWhile {
            it < 5
        }.collect {
            println(it)
        }
}

/*
Note
 collect, reduce, fold, toList, toSet과 같은 연산자는 플로우를 끝내는 함수라 종단 연산자(terminal operator)라고 함
 -> reduce는 흔히 map과 reduce로 함께 소개되는 함수형 언어의 오래된 메커니즘
    첫번째 값을 결과에 넣은 후 각 값을 가져와 누진적으로 계산합니다.

TODO
 reduce 연산자

me
 누진적 -> 약간 자바 중첩 덧셉 문제랑 비슷한거 같기도
 */
fun main12() = runBlocking {
    val value = (1..10)
        .asFlow()  // 1, 2, 3, 4, 5, 6, ,7 , 8 , 9, 10
        .reduce { a, b -> // a:1 , b:2 -> a:3 , b:3 -> a:6 , b:4  = 이렇게 설정한 숫자가 더해지며 b는 다음 숫자를 가져옴
            a + b
        }
    println(value)
}

/*
Note
 fold = reduce와 매우 유사하지만 초기값이 있다는 차이만 있음

TODO
 fold 연산자
 */
fun main13() = runBlocking {
    val value = (1..10) // 1, 2, 3 ~
        .asFlow()
        .fold(10) { a, b -> // 초기값 a:10, b:1
            a + b
        } // 합 : 65
}

/*
Note
 count = 말 그래도 갯수를 출력하는 것

TODO
 count 연산자
 */
fun main14() = runBlocking {
    val counter = (1..10)
        .asFlow()
        .count {
            (it % 2) == 0
        }
    println(counter)
}