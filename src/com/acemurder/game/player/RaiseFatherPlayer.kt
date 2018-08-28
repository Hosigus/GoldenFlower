package com.acemurder.game.player

import com.acemurder.game.Player
import com.acemurder.game.Poker
import com.acemurder.game.RoundInfo

class RaiseFatherPlayer : Player {
    override val name = "加注狗"
    override val stuNum = "20152118764"

    //爸爸给我说玩牌要霸气，不要看牌，直接加倍
    override fun bet(info: RoundInfo, hands: Array<Poker>, minMoneyNeedToPay: Int) = 3 * minMoneyNeedToPay

}