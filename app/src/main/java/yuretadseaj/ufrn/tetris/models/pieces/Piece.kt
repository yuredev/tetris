package yuretadseaj.ufrn.tetris.models.pieces

import yuretadseaj.ufrn.tetris.models.Point

enum class Colors{
    WHITE,
    GREEN,
    PURPLE,
    YELLOW,
    CYAN,
    ORANGE,
    RED,
    BLUE
}

open class Piece(
    var pointA: Point,
    var pointB: Point,
    var pointC: Point,
    var pointD: Point
) {
    var color = when ((Math.random() * 8).toInt()) {
        0 -> Colors.CYAN
        1 -> Colors.GREEN
        2 -> Colors.PURPLE
        3 -> Colors.YELLOW
        4 -> Colors.WHITE
        5 -> Colors.ORANGE
        6 -> Colors.BLUE
        7 -> Colors.RED
        else -> Colors.WHITE
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

    open fun rotate(): Piece {
        return this
    }

    open fun rotated(): Piece {
        return Piece(
            Point(pointA),
            Point(pointB),
            Point(pointC),
            Point(pointD)
        )
    }

    fun getPoints(): Array<Point> {
        return arrayOf(Point(pointA), Point(pointB), Point(pointC), Point(pointD))
    }
}
