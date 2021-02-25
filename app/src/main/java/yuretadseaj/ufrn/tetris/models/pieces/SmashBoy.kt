package yuretadseaj.ufrn.tetris.models.pieces

import yuretadseaj.ufrn.tetris.models.Point

class SmashBoy(initialPosition: Point) : Piece(
    Point(initialPosition.row, initialPosition.column),
    Point(initialPosition.row, initialPosition.column + 1),
    Point(initialPosition.row + 1, initialPosition.column),
    Point(initialPosition.row + 1, initialPosition.column + 1)
)