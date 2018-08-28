package com.acemurder.game

import java.io.File

private const val FILE_PATH = "src/com/acemurder/game/player/"
private const val PACKAGE_NAME_PREFIX = "com.acemurder.game.player."

fun main(args: Array<String>) {
    val file = File(FILE_PATH)
    val files = file.listFiles() ?: return
    val players = mutableListOf<Player>()
    for (f in files) {
        val className = f.name.replace(".kt", "").replace(".java", "")
        try {
            val clazz = Class.forName(PACKAGE_NAME_PREFIX + className) as Class<*>
            val p = clazz.newInstance()
            (p as? Player)?.let { players.add(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    val manager = Manager(players)
    manager.startGame(INIT_MONEY / MIN_MONEY_PER_TIME * 3 / 2)
}