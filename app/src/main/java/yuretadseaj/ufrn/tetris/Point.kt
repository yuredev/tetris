package yuretadseaj.ufrn.tetris

data class Point(var row: Int, var column: Int) {
    constructor(point: Point): this(point.row, point.column)
    fun moveDown(): Point {
        row++
        return this
    }
    fun moveUp(): Point {
        row--
        return this
    }
    fun moveRight(): Point {
        column++
        return this
    }
    fun moveLeft(): Point {
        column--
        return this
    }

}