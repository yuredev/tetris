package yuretadseaj.ufrn.tetris

data class Position(var row: Int, var column: Int) {
    fun moveDown(): Position {
        row++
        return this
    }
    fun moveUp(): Position {
        row--
        return this
    }
    fun moveRight(): Position {
        column++
        return this
    }
    fun moveLeft(): Position {
        column--
        return this
    }
}