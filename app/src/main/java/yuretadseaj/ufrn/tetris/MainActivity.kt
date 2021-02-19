package yuretadseaj.ufrn.tetris

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import yuretadseaj.ufrn.tetris.databinding.ActivityMainBinding
import yuretadseaj.ufrn.tetris.pieces.Hero
import yuretadseaj.ufrn.tetris.pieces.Piece
import yuretadseaj.ufrn.tetris.pieces.SmashBoy

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var currentPiece: Piece
    private val rowCount = 22
    private val columnCount = 12
    private var isRuning = true
    private val waitingTime = 300
    private val initialPosition = Point(1, 1)
    private val stoppedPieces = mutableListOf<Piece>()

    private val validPositionsBoard = Array(rowCount) {
        Array(columnCount) {
            true
        }
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
            if (piecePositionIsValid(currentPiece.toRight())) {
                currentPiece.moveRight()
            }
        }
        binding.btnLeft.setOnClickListener {
            if (piecePositionIsValid(currentPiece.toLeft())) {
                currentPiece.moveLeft()
            }
        }
        binding.btnDown.setOnClickListener {
            if (piecePositionIsValid(currentPiece.toDown())) {
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
        when ((Math.random() * 2).toInt()) {
            0 -> return SmashBoy(initialPosition)
            1 -> return Hero(initialPosition)
        }
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
                    validPositionsBoard[i][j] = false
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

    private fun positionIsBusy(point: Point): Boolean {
        return !validPositionsBoard[point.row][point.column]
    }

    private fun piecePositionIsValid(piece: Piece): Boolean {
        for (point in piece.getPoints()) {
            if (isBorder(point) || positionIsBusy(point)) {
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

    private fun renderStoppedPieces() {
        for (piece in stoppedPieces) {
            for ((row, column) in piece.getPoints()) {
                boardView[row][column]!!.setImageResource(R.drawable.white_point)
            }
        }
    }

    private fun run() {
        Thread {
            while (isRuning) {
                Thread.sleep(waitingTime.toLong())
                runOnUiThread {
                    clearScreen()
                    renderStoppedPieces()
                }
                if (piecePositionIsValid(currentPiece.toDown())) {
                    currentPiece.moveDown()
                } else {
                    stoppedPieces.add(currentPiece)
                    currentPiece.getPoints().forEach {(row, column) ->
                        validPositionsBoard[row][column] = false
                    }
                    currentPiece = getRandomPiece()
                }
                runOnUiThread {
                    renderPiece(currentPiece)
                }
            }
        }.start()
    }
}
