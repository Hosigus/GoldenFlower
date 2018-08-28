package com.acemurder.game.player

import com.acemurder.game.Player
import com.acemurder.game.Poker
import com.acemurder.game.RoundInfo

class CallGodPlayer : Player {
    override val name = "跟注帝"
    override val stuNum = "20152118761"

    override fun bet(info: RoundInfo, hands: Array<Poker>, minMoneyNeedToPay: Int) = minMoneyNeedToPay

}