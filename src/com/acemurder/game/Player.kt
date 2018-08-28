package com.acemurder.game

interface Player {
    val name: String
    val stuNum: String
    fun getInfo() = name + stuNum
    fun onGameStart(headcount: Int){}
    fun bet(info: RoundInfo, hands: Array<Poker>,minMoneyNeedToPay: Int): Int
    fun onResult(time: Int, isWinner: Boolean, hands: Array<Poker>){}
}