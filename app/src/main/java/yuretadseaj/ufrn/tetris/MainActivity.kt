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
    private val rowCount = 36
    private val columnCount = 36
    private var isRuning = true
    private val speed = 300
    private val initialPosition = Point(0, 0)
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
        inflateGameArea()
        run()
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

    private fun clearScreen() {
        for (i in 0 until rowCount) {
            for (j in 0 until columnCount) {
                boardView[i][j]!!.setImageResource(R.drawable.black_point)
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
            if (point.row == rowCount) {
                return false
            }
        }
        return true
    }

    private fun run() {
        Thread {
            while (isRuning) {
                Thread.sleep(speed.toLong())
                runOnUiThread {
                    clearScreen()
                    val (pointA, pointB, pointC, pointD) = currentPiece.getPoints()
                    val futurePiece = Piece(pointA, pointB, pointC, pointD).moveDown()
                    if (piecePositionIsValid(futurePiece)) {
                        currentPiece.moveDown()
                    }
                    renderPiece(currentPiece)
                }
            }
        }.start()
    }
}
