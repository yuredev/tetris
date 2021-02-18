package yuretadseaj.ufrn.tetris

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import yuretadseaj.ufrn.tetris.databinding.ActivityMainBinding
import yuretadseaj.ufrn.tetris.pieces.Piece
import yuretadseaj.ufrn.tetris.pieces.SmashBoy

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val rowCount = 22
    private val columnCount = 12
    private var isRuning = true
    private val speed = 300
    private val initialPosition = Point(1, 1)
    private lateinit var currentPiece: Piece

    private val board = Array(rowCount) {
        Array(columnCount) {}
    }

    private val boardView = Array(rowCount) {
        arrayOfNulls<ImageView>(columnCount)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.gameArea.rowCount = rowCount
        binding.gameArea.columnCount = columnCount
        currentPiece = getRandomPiece()
        defineBtnActions()
        inflateGameArea()
        run()
    }

    private fun defineBtnActions() {
        binding.btnRight.setOnClickListener {
            val (pointA, pointB, pointC, pointD) = currentPiece.getPoints()
            val futurePiece = Piece(pointA, pointB, pointC, pointD).moveRight()
            if (piecePositionIsValid(futurePiece)) {
                currentPiece.moveRight()
            }
        }
        binding.btnLeft.setOnClickListener {
            val (pointA, pointB, pointC, pointD) = currentPiece.getPoints()
            val futurePiece = Piece(pointA, pointB, pointC, pointD).moveLeft()
            if (piecePositionIsValid(futurePiece)) {
                currentPiece.moveLeft()
            }
        }
        binding.btnDown.setOnClickListener {
            val (pointA, pointB, pointC, pointD) = currentPiece.getPoints()
            val futurePiece = Piece(pointA, pointB, pointC, pointD).moveDown()
            if (piecePositionIsValid(futurePiece)) {
                currentPiece.moveDown()
            }
        }
    }

    private fun inflateGameArea() {
        val inflater = LayoutInflater.from(this)
        for (i in 0 until rowCount) {
            for (j in 0 until columnCount) {
                boardView[i][j] = inflater.inflate(
                        R.layout.inflate_image_view,
                        binding.gameArea,
                        false
                ) as ImageView
                binding.gameArea.addView(boardView[i][j])
            }
        }
    }

    private fun getRandomPiece(): Piece {
        return SmashBoy(initialPosition)
    }

    private fun isBorder(point: Point): Boolean {
        val (row, column) = point
        return row == 0 || column == 0 || row == rowCount - 1 || column == columnCount - 1
    }

    private fun clearScreen() {
        for (i in 0 until rowCount) {
            for (j in 0 until columnCount) {
                if (isBorder(Point(i, j))) {
                    boardView[i][j]!!.setImageResource(R.drawable.gray_point)
                } else {
                    boardView[i][j]!!.setImageResource(R.drawable.black_point)
                }
            }
        }
    }

    private fun renderPiece(piece: Piece) {
        for (pos in piece.getPoints()) {
            try {
                boardView[pos.row][pos.column]!!.setImageResource(R.drawable.white_point)
            } catch (e: ArrayIndexOutOfBoundsException) {
                isRuning = false
            }
        }
    }

    private fun piecePositionIsValid(piece: Piece): Boolean {
        for (point in piece.getPoints()) {
            if (isBorder(point)) {
                return false
            }
        }
        return true
    }

    private fun isInBoardBottom(piece: Piece): Boolean {
        for (point in piece.getPoints()) {
            if (point.row == rowCount - 2) {
                return true
            }
        }
        return false
    }

    private fun run() {
        Thread {
            while (isRuning) {
                Thread.sleep(speed.toLong())
                runOnUiThread {
                    clearScreen()
                }
                val (pointA, pointB, pointC, pointD) = currentPiece.getPoints()
                val futurePiece = Piece(pointA, pointB, pointC, pointD).moveDown()
                if (piecePositionIsValid(futurePiece)) {
                    currentPiece.moveDown()
                } else {
                    if (isInBoardBottom(currentPiece)) {
                        currentPiece = getRandomPiece()
                    }
                }
                runOnUiThread {
                    renderPiece(currentPiece)
                }
            }
        }.start()
    }
}
