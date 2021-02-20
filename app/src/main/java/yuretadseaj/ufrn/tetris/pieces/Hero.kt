package yuretadseaj.ufrn.tetris.pieces

import yuretadseaj.ufrn.tetris.Point

class Hero(initialPosition: Point) : Piece(
        Point(initialPosition.row, initialPosition.column - 1),
        Point(initialPosition.row, initialPosition.column),
        Point(initialPosition.row, initialPosition.column + 1),
        Point(initialPosition.row, initialPosition.column + 2)
) {

    constructor(hero: Hero) : this(hero.pointB)

    override fun rotate(): Hero {
        if (isInHorizontal()) {
            pointD.column = pointC.column
            pointB.column = pointC.column
            pointA.column = pointB.column
            pointD.row = pointC.row - 1
            pointB.row = pointC.row + 1
            pointA.row = pointC.row + 2
        } else {
            pointD.row = pointC.row
            pointB.row = pointC.row
            pointA.row = pointB.row
            pointD.column = pointC.column + 1
            pointB.column = pointC.column - 1
            pointA.column = pointC.column - 2
        }
        return this
    }

    private fun isInHorizontal(): Boolean {
        return (pointA.row == pointB.row && pointB.row == pointC.row && pointC.row == pointD.row)
    }

    override fun rotated(): Hero {
        val heroCopy = Hero(this)
        return heroCopy.rotate()
    }
}