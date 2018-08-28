package com.acemurder.game.player

import com.acemurder.game.Player
import com.acemurder.game.Poker
import com.acemurder.game.RoundInfo

class HosigusPlayer : Player {
    override val name: String = "Hosigus"
    override val stuNum: String = "2017211803"

    override fun bet(info: RoundInfo, hands: Array<Poker>, minMoneyNeedToPay: Int): Int {
        return 0
//        TODO("反射啊，打什么牌啊")
    }
}