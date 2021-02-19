package yuretadseaj.ufrn.tetris.pieces

import yuretadseaj.ufrn.tetris.Point

class Hero(initialPosition: Point) : Piece(
        Point(initialPosition.row, initialPosition.column),
        Point(initialPosition.row, initialPosition.column + 1),
        Point(initialPosition.row, initialPosition.column + 2),
        Point(initialPosition.row, initialPosition.column + 3)
) {
    override fun flip() {
        super.flip()
    }
}