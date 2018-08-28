package com.acemurder.game.player

import com.acemurder.game.Player
import com.acemurder.game.Poker
import com.acemurder.game.RoundInfo

class FoldPlayer : Player {
    override val name = "弃牌狗"
    override val stuNum = "20152118762"

    //妈妈说不要赌博，我要弃牌
    override fun bet(info: RoundInfo, hands: Array<Poker>, minMoneyNeedToPay: Int) = 0

}