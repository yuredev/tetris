package yuretadseaj.ufrn.tetris.pieces

import yuretadseaj.ufrn.tetris.Position

class SmashBoy(initialPosition: Position) : Piece {

    override var positionA: Position = Position(initialPosition.row, initialPosition.column)
    override var positionB: Position = Position(initialPosition.row, initialPosition.column + 1)
    override var positionC: Position = Position(initialPosition.row + 1, initialPosition.column)
    override var positionD: Position = Position(initialPosition.row + 1, initialPosition.column + 1)

    override fun moveUp() {
        positionA.moveUp()
        positionB.moveUp()
        positionC.moveUp()
        positionD.moveUp()
    }

    override fun moveRight() {
        positionA.moveRight()
        positionB.moveRight()
        positionC.moveRight()
        positionD.moveRight()
    }

    override fun moveLeft() {
        positionA.moveLeft()
        positionB.moveLeft()
        positionC.moveLeft()
        positionD.moveLeft()
    }

    override fun moveDown() {
        positionA.moveDown()
        positionB.moveDown()
        positionC.moveDown()
        positionD.moveDown()
    }

    override fun flip() {
        return
    }
}
