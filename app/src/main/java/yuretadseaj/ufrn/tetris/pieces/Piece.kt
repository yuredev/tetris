package yuretadseaj.ufrn.tetris.pieces

import yuretadseaj.ufrn.tetris.Point

open class Piece(
        private var pointA: Point,
        private var pointB: Point,
        private var pointC: Point,
        private var pointD: Point
) {
    fun moveUp(): Piece {
        pointA.moveUp()
        pointB.moveUp()
        pointC.moveUp()
        pointD.moveUp()
        return this
    }

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

    open fun flip() {
        return
    }

    fun getPoints(): Array<Point> {
        return arrayOf(Point(pointA), Point(pointB), Point(pointC), Point(pointD))
    }
}
