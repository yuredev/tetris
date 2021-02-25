package yuretadseaj.ufrn.tetris.models.pieces

import yuretadseaj.ufrn.tetris.models.Point

class Hero : Piece {
    constructor(initialPosition: Point): super(
        Point(initialPosition.row, initialPosition.column - 1),
        Point(initialPosition.row, initialPosition.column),
        Point(initialPosition.row, initialPosition.column + 1),
        Point(initialPosition.row, initialPosition.column + 2)
    )
    constructor(
        pointA: Point,
        pointB: Point,
        pointC: Point,
        pointD: Point
    ): super(pointA, pointB, pointC, pointD)

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

    override fun rotated(): Piece {
        val pieceCopy = Hero(
            Point(pointA),
            Point(pointB),
            Point(pointC),
            Point(pointD)
        )
        pieceCopy.color = color
        return pieceCopy.rotate()
    }

    private fun isInHorizontal(): Boolean {
        return (pointA.row == pointB.row && pointB.row == pointC.row && pointC.row == pointD.row)
    }
}