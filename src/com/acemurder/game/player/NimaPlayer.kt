package com.acemurder.game.player

import com.acemurder.game.Manager
import com.acemurder.game.Player
import com.acemurder.game.Poker
import com.acemurder.game.RoundInfo

import java.util.Collections

/**
 * Created by : AceMurder
 * Created on : 2017/11/6
 * Created for : Games.
 * Enjoy it !!!!
 */
class NimaPlayer : Player {
    override fun onGameStart(headcount: Int) {}

    override fun bet(info: RoundInfo, hands: Array<Poker>, minMoneyNeedToPay: Int): Int {
        hands.sort()
        val (_, round, _, moneyOnDesk) = info
        return if (isSameColor(hands))
            if (((2 + round / 10f) * minMoneyNeedToPay).toInt() < 3 * moneyOnDesk) ((2 + round / 10f) * minMoneyNeedToPay).toInt() else 3 * moneyOnDesk - 1
        else if (isSameColorStraight(hands) || isSamePoint(hands))
            if (((2 + round / 10f) * minMoneyNeedToPay).toInt() < 3 * moneyOnDesk) ((2 + round / 10f) * minMoneyNeedToPay).toInt() else 3 * moneyOnDesk - 1
        else if (isPair(hands))
            if ((1.3 * minMoneyNeedToPay).toInt() < 3 * moneyOnDesk) (1.3 * minMoneyNeedToPay).toInt() else minMoneyNeedToPay

        else if (isStraight(hands))
            if ((1.7 * minMoneyNeedToPay).toInt() < 3 * moneyOnDesk) (1.7 * minMoneyNeedToPay).toInt() else minMoneyNeedToPay
        else minMoneyNeedToPay.takeIf { hands.any { it.point.ordinal > 9 } } ?: 0
    }

    override val name = "王尼玛"

    override val stuNum = "20152118763"


    private fun isSameColor(pokers: Array<Poker>) = pokers[0].suit === pokers[1].suit
            && pokers[1].suit === pokers[2].suit


    private fun isPair(pokers: Array<Poker>)= (pokers[1].point == pokers[0].point
                || pokers[1].point == pokers[2].point
                || pokers[0].point == pokers[2].point)

    private fun isStraight(pokers: Array<Poker>) = Math.abs(pokers[0].point.ordinal - pokers[1].point.ordinal) == 1
            && Math.abs(pokers[1].point.ordinal - pokers[2].point.ordinal) == 1

    private fun isSameColorStraight(handCards: Array<Poker>) = isSameColor(handCards)
            && isStraight(handCards)

    private fun isSamePoint(handCards: Array<Poker>) = handCards[0].point == handCards[1].point
            && handCards[2].point == handCards[1].point
}
