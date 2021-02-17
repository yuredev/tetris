package yuretadseaj.ufrn.tetris.pieces

import yuretadseaj.ufrn.tetris.Position

interface Piece {
    var positionA: Position
    var positionB: Position
    var positionC: Position
    var positionD: Position

    fun moveUp()
    fun moveRight()
    fun moveLeft()
    fun moveDown()
    fun flip()
    fun getPositions() = arrayOf(positionA, positionB, positionC, positionD)
}
