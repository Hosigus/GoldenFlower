package com.acemurder.game

import kotlin.collections.HashMap

data class RoundInfo(
        val time: Int,
        val round: Int,
        val lastPerson: Int,
        val moneyOnDesk: Int)

const val MIN_MONEY_PER_TIME = 100
const val INIT_MONEY = 200000

private val SAME_POINT = 60 * 100000
private val STRAIGHT = 20 * 100000
private val SAME_COLOR = 30 * 100000
private val SAME_COLOR_STRAIGHT = STRAIGHT + SAME_COLOR
private val PAIR = 10 * 100000

class Manager(private val players: List<Player>){

    private val handCardsMap = HashMap<String, List<Poker>>()
    private val jettonMap = HashMap<String, Int>()
    private val playTimeMap = HashMap<String, Int>()

    private var totalTimes:Int=0

    init {
        players.forEach {
            jettonMap[it.stuNum] = INIT_MONEY
        }
    }

    private var Player.time:Int
        get() = playTimeMap[stuNum] ?: 0
        set(value){
            playTimeMap[stuNum] = value
        }

    private var Player.jetton: Int
        get() = jettonMap[stuNum] ?: 0
        set(value){
            jettonMap[stuNum] = value
        }

    private var Player.handCards: List<Poker>
        get() = handCardsMap[stuNum] ?: listOf()
        set(value) {
            handCardsMap[stuNum] = value
        }

    fun startGame(times: Int) {
        totalTimes = times
        players.forEach {
            it.time = totalTimes
            it.onGameStart(players.size)
        }
        for (i in 1..totalTimes) {
            play(i)
        }
        onMatchEnd()
    }

    private fun onMatchEnd() {
        println("游戏结束：")
        jettonMap.entries
                .sortedBy { it.value }
                .forEach {
                    println(" ${it.key} : ${it.value} 剩余筹码, 挣扎了${playTimeMap[it.key]}局")
                }

    }

    private fun play(time: Int) {
        var moneyOnDesk = 0
        var minMoneyNeedToPay = MIN_MONEY_PER_TIME

        val playersOnMatch = players
                .asSequence()
                .filter {
                    if (it.jetton >= minMoneyNeedToPay) {
                        true
                    } else {
                        if (it.time == totalTimes) {
                            it.time = time + 1
                        }
                        false
                    }
                }.onEach {
                    it.jetton -= minMoneyNeedToPay
                }.toList()

        val playersOnTable = playersOnMatch.toMutableList()

        playersOnTable.shuffle()

        val pokers = createPokers(playersOnTable.count())

        playersOnTable.forEachIndexed { index, player ->
            player.handCards = List(3) {
                pokers[3 * index + it]
            }
        }

        playersOnTable.shuffle()

        var round = 0
        println("第${time}局游戏开始：游戏人数：${playersOnTable.size}")
        while (//加注循环
            round < 5 && playersOnTable.size > 1 &&
            moneyOnDesk < playersOnMatch.size * 5000
        ) {
            println("   第" + round + "轮开始：")
            val roundInfo = RoundInfo(time, round, playersOnTable.size, moneyOnDesk)
            val outList = mutableListOf<Player>()
            playersOnTable.forEach{
                val jetton = it.bet(roundInfo, it.handCards.toTypedArray(), minMoneyNeedToPay)
                if (jetton > it.jetton || jetton < minMoneyNeedToPay || jetton > 3 * minMoneyNeedToPay) {
                    println("     玩家 ${it.getInfo()} 弃牌")
                    outList.add(it)
                } else {
                    it.jetton -= jetton
                    println("     玩家 ${it.getInfo()} 下注$jetton")
                    moneyOnDesk += jetton
                    minMoneyNeedToPay = jetton
                }
            }
            playersOnTable.removeAll(outList)
            round++
        }
        println(" 本局游戏结束:")
        val winner = playersOnTable.maxBy { judge(it.handCards) }
        playersOnMatch.forEach {
            val handCards = it.handCards.toTypedArray()
            if (it == winner) {
                it.jetton += moneyOnDesk
                it.onResult(time, true, handCards)
            } else {
                it.onResult(time, false, handCards)
            }
            println("  ${it.getInfo()} 的手牌是${handCards[0]} ${handCards[1]} ${handCards[2]}   余额：${it.jetton}")
        }

        if (winner != null) {
            val handCards = winner.handCards
            println(" ${winner.getInfo()}获得了${moneyOnDesk}筹码，手牌是${handCards[0]} ${handCards[1]} ${handCards[2]}\n")
        }
    }

    private fun judge(handCards: List<Poker>): Int {
        handCards.sortedDescending()
        return when (handCards) {
            ::isSamePoint -> SAME_POINT
            ::isSameColorStraight -> SAME_COLOR_STRAIGHT
            ::isSameColor -> SAME_COLOR
            ::isStraight -> STRAIGHT
            ::isPair -> PAIR
            else -> 0
        } + if (isPair(handCards)) {
            val (same, other) = findPair(handCards)
            same * 10100 + other
        } else {
            val ordinals = handCards.map { it.point.ordinal }
            ordinals[0] * 10000 + ordinals[1] * 100 + ordinals[2]
        }
    }

    private fun findPair(handCards: List<Poker>) =
            if (handCards[0].point == handCards[1].point) {
                0 to 2
            }else if (handCards[0].point == handCards[2].point) {
                0 to 1
            } else {
                1 to 0
            }

    private fun isSameColor(handCards: List<Poker>) = handCards.all { it.suit == handCards[0].suit }

    private fun isPair(handCards: List<Poker>) = handCards.any { thisOne->
        handCards.firstOrNull { it != thisOne && it.point == thisOne.point } != null
    }

    private fun isStraight(handCards: List<Poker>) =
            handCards[2].point.ordinal - handCards[1].point.ordinal == 1 &&
            handCards[1].point.ordinal - handCards[0].point.ordinal == 1

    private fun isSameColorStraight(handCards: List<Poker>): Boolean {
        return isSameColor(handCards) && isStraight(handCards)
    }

    private fun isSamePoint(handCards: List<Poker>) = handCards.all { it.point == handCards[0].point }

    private fun createPokers(playerCount: Int): List<Poker> =
            mutableListOf<Poker>().apply {
                for (i in 0..playerCount * 3 / 52) {
                    Point.values().forEach {point ->
                        Suit.values().forEach {suit ->
                            add(Poker(suit, point))
                        }
                    }
                }
                shuffle()
            }

}
