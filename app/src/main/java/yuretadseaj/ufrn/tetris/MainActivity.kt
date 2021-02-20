package yuretadseaj.ufrn.tetris

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import yuretadseaj.ufrn.tetris.databinding.ActivityMainBinding
import yuretadseaj.ufrn.tetris.pieces.Colors
import yuretadseaj.ufrn.tetris.pieces.Hero
import yuretadseaj.ufrn.tetris.pieces.Piece
import yuretadseaj.ufrn.tetris.pieces.SmashBoy
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var currentPiece: Piece
    private val rowCount = 23
    private val columnCount = 13
    private var isRunning = true
    private val waitingTime = 250
    private val initialPosition = Point(0, columnCount / 2 - 1)
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
        if (currentPiece is SmashBoy) {
            binding.btnFlip.visibility = View.INVISIBLE
        }
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
        binding.btnFlip.setOnClickListener {
            val isValid = piecePositionIsValid(currentPiece.rotated())
            if (isValid) {
                currentPiece.rotate()
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
        return when (Random.nextInt(0, 1 + 1)) {
            0 -> SmashBoy(initialPosition)
            1 -> Hero(initialPosition)
            else -> SmashBoy(initialPosition)
        }
    }

    private fun isBorder(point: Point): Boolean {
        val (row, column) = point
        return row == 0 || column == 0 || row == rowCount - 1 || column == columnCount - 1
    }

    private fun isOutOfBounds(point: Point): Boolean {
        val (row, column) = point
        return row < 0 || column < 0 || row > rowCount - 1 || column > columnCount - 1
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
                boardView[pos.row][pos.column]!!.setImageResource(getPieceColor(currentPiece))
            } catch (e: ArrayIndexOutOfBoundsException) {
                isRunning = false
            }
        }
    }

    private fun getPieceColor(piece: Piece): Int {
        return when (piece.color) {
            Colors.WHITE -> R.drawable.white_point
            Colors.PURPLE -> R.drawable.purple_point
            Colors.GREEN -> R.drawable.green_point
            Colors.CYAN -> R.drawable.cyan_point
            Colors.YELLOW -> R.drawable.yellow_point
            Colors.ORANGE -> R.drawable.orange_point
            Colors.BLUE -> R.drawable.blue_point
            Colors.RED -> R.drawable.red_point
            else -> R.drawable.white_point
        }
    }
    private fun isBusy(point: Point): Boolean {
        return !validPositionsBoard[point.row][point.column]
    }

    private fun piecePositionIsValid(piece: Piece): Boolean {
        for (point in piece.getPoints()) {
            if (isOutOfBounds(point) || isBorder(point) || isBusy(point)) {
                return false
            }
        }
        return true
    }

    private fun renderStoppedPieces() {
        for (piece in stoppedPieces) {
            for ((row, column) in piece.getPoints()) {
                boardView[row][column]!!.setImageResource(getPieceColor(piece))
            }
        }
    }

    private fun run() {
        Thread {
            while (isRunning) {
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
                    runOnUiThread {
                        if (currentPiece is SmashBoy) {
                            binding.btnFlip.visibility = View.INVISIBLE
                        } else {
                            binding.btnFlip.visibility = View.VISIBLE
                        }
                    }
                }
                runOnUiThread {
                    renderPiece(currentPiece)
                }
            }
        }.start()
    }
}
