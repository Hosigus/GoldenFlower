package com.acemurder.game

enum class Suit(val desc: String){
    SPADE("♠"), CLUB("♣"), HEART("♥"), DIAMOND("♦");
}

enum class Point{
    _2,_3,_4,_5,_6,_7,_8,_9,_10,J,Q,K,A;
}

data class Poker(val suit: Suit, val point: Point) : Comparable<Poker> {

    override fun compareTo(other: Poker): Int = point.compareTo(other.point)

    override fun toString(): String = suit.desc + point

}