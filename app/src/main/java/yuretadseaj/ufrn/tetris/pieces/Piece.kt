package yuretadseaj.ufrn.tetris.pieces

import yuretadseaj.ufrn.tetris.Point

open class Piece(
        var pointA: Point,
        var pointB: Point,
        var pointC: Point,
        var pointD: Point
) {
    fun moveRight(): Piece {
        pointA.moveRight()
        pointB.moveRight()
        pointC.moveRight()
        pointD.moveRight()
        return this
    }

    fun moveLeft(): Piece {
        pointA.moveLeft()
        pointB.moveLeft()
        pointC.moveLeft()
        pointD.moveLeft()
        return this
    }

    fun moveDown(): Piece {
        pointA.moveDown()
        pointB.moveDown()
        pointC.moveDown()
        pointD.moveDown()
        return this
    }

    fun toLeft() = Piece(
            Point(pointA),
            Point(pointB),
            Point(pointC),
            Point(pointD)
    ).moveLeft()

    fun toDown() = Piece(
            Point(pointA),
            Point(pointB),
            Point(pointC),
            Point(pointD)
    ).moveDown()

    fun toRight() = Piece(
            Point(pointA),
            Point(pointB),
            Point(pointC),
            Point(pointD)
    ).moveRight()

    open fun rotated(): Piece {
        return this
    }

    open fun rotate(): Piece {
        return this
    }

    fun getPoints(): Array<Point> {
        return arrayOf(Point(pointA), Point(pointB), Point(pointC), Point(pointD))
    }
}
