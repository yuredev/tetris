package yuretadseaj.ufrn.tetris.models.pieces

import yuretadseaj.ufrn.tetris.models.Point

class BlueRicky: Piece {
    private var initialPosition: Point

    constructor(initialPosition: Point): super(
        Point(initialPosition.row, initialPosition.column),
        Point(initialPosition.row + 1, initialPosition.column),
        Point(initialPosition.row + 1, initialPosition.column + 1),
        Point(initialPosition.row + 1, initialPosition.column + 2)
    ) {
        this.initialPosition = initialPosition
    }

    constructor(
        pointA: Point,
        pointB: Point,
        pointC: Point,
        pointD: Point
    ): super(pointA, pointB, pointC, pointD) {
        this.initialPosition = Point(pointA)
    }

    private var currentStep: Int = 1

    private fun step1() {
        pointA.column++
        pointB.row--
        pointC.column--
        pointD.column -= 2
        pointD.row++
    }

    private fun step2() {
        pointA.column++
        pointA.row++
        pointB.column += 2
        pointC.row--
        pointC.column++
        pointD.row -= 2
    }

    private fun step3() {
        pointA.row++
        pointA.column--
        pointB.row += 2
        pointC.row++
        pointC.column++
        pointD.column += 2
    }

    private fun restart() {
        val newPiece = BlueRicky(initialPosition)
        val currentRow = pointC.row

        pointA = newPiece.pointA
        pointB = newPiece.pointB
        pointC = newPiece.pointC
        pointD = newPiece.pointD

        pointA.row = currentRow
        pointB.row = currentRow + 1
        pointC.row = currentRow + 1
        pointD.row = currentRow + 1
    }

    override fun rotate(): BlueRicky {
        when (currentStep) {
            1 -> step1()
            2 -> step2()
            3 -> step3()
            4 -> restart()
        }
        if (++currentStep > 4) {
            currentStep = 1
        }
        return this
    }

    override fun rotated(): Piece {
        val pieceCopy = BlueRicky(
            Point(pointA),
            Point(pointB),
            Point(pointC),
            Point(pointD)
        )
        return pieceCopy.rotate()
    }
}