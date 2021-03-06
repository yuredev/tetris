package yuretadseaj.ufrn.tetris

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import yuretadseaj.ufrn.tetris.databinding.ActivityMainBinding
import yuretadseaj.ufrn.tetris.models.Point
import yuretadseaj.ufrn.tetris.models.pieces.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var currentPiece: Piece
    private var waitingTime: Int = 400
    private val rowCount = 24
    private val columnCount = 12
    private val initialPosition = Point(1, columnCount / 2 - 1)
    private var isRunning = true
    private var score: Long = 0
    private var busyPoints = mutableListOf<HashMap<String, Any>>()

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
        waitingTime = getWaitingTimeByDifficulty()
        defineBtnActions()
        inflateGameArea()
        run()
    }

    private fun getWaitingTimeByDifficulty(): Int {
        val difficulty = getSharedPreferences("${R.string.app_name}_settings", Context.MODE_PRIVATE)
            .getString("difficulty", "")
        return when(difficulty) {
            "easy" -> 325
            "medium" -> 225
            "hard" -> 125
            else -> 200
        }
    }

    private fun defineBtnActions() {
        binding.btnRight.setOnClickListener {
            if (piecePositionIsValid(currentPiece.toRight())) {
                currentPiece.moveRight()
                refreshScreen()
            }
        }
        binding.btnLeft.setOnClickListener {
            if (piecePositionIsValid(currentPiece.toLeft())) {
                currentPiece.moveLeft()
                refreshScreen()
            }
        }
        binding.btnDown.setOnClickListener {
            if (piecePositionIsValid(currentPiece.toDown())) {
                currentPiece.moveDown()
                refreshScreen()
            }
        }
        binding.btnFlip.setOnClickListener {
            if (piecePositionIsValid(currentPiece.rotated())) {
                currentPiece.rotate()
                refreshScreen()
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
        return when ((Math.random() * 3).toInt()) {
            0 -> Hero(initialPosition)
            1 -> SmashBoy(initialPosition)
            2 -> BlueRicky(initialPosition)
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
        for ((row, column) in piece.getPoints()) {
            boardView[row][column]!!.setImageResource(getColorResource(piece.color))
        }
    }

    private fun getColorResource(color: Colors): Int {
        return when (color) {
            Colors.WHITE -> R.drawable.white_point
            Colors.PURPLE -> R.drawable.purple_point
            Colors.GREEN -> R.drawable.green_point
            Colors.CYAN -> R.drawable.cyan_point
            Colors.YELLOW -> R.drawable.yellow_point
            Colors.ORANGE -> R.drawable.orange_point
            Colors.BLUE -> R.drawable.blue_point
            Colors.RED -> R.drawable.red_point
        }
    }

    private fun piecePositionIsValid(piece: Piece): Boolean {
        for (point in piece.getPoints()) {
            if (isOutOfBounds(point) || isBorder(point) || isBusy(point)) {
                return false
            }
        }
        return true
    }

    private fun isBusy(point: Point): Boolean {
        return !validPositionsBoard[point.row][point.column]
    }

    private fun renderBusyPoints() {
        for (busyPoint in busyPoints) {
            val pointColor = busyPoint["color"] as Colors
            val (row, column) = busyPoint["point"] as Point
            boardView[row][column]!!.setImageResource(getColorResource(pointColor))
        }
    }

    private fun refreshScreen() {
        clearScreen()
        renderBusyPoints()
        renderPiece(currentPiece)
    }

    private fun getScoredRows(): MutableList<Int> {
        val rowsScored = mutableListOf<Int>()
        for (i in 1 until rowCount - 1) {
            var scored = true
            for (j in 1 until columnCount - 1) {
                if (!isBusy(Point(i, j))) {
                    scored = false
                    break
                }
            }
            if (scored) {
                rowsScored.add(i)
            }
        }
        return rowsScored
    }

    private fun destroyRow(row: Int) {
        for (column in 1 until columnCount - 1) {
            validPositionsBoard[row][column] = true
            busyPoints = busyPoints.filter {
                val point = it["point"] as Point
                point.row != row
            }.toMutableList()
        }
        busyPoints = busyPoints.map {
            var result = it
            val point = it["point"] as Point
            if (point.row < row) {
                val color = it["color"] as Colors
                validPositionsBoard[point.row][point.column] = true
                point.moveDown()
                validPositionsBoard[point.row][point.column] = false
                result = hashMapOf("point" to point, "color" to color)
            }
            result
        }.toMutableList()
        refreshScreen()
    }

    private fun run() {
        Thread {
            while (isRunning) {
                Thread.sleep(waitingTime.toLong())
                runOnUiThread {
                    refreshScreen()
                }
                if (piecePositionIsValid(currentPiece.toDown())) {
                    currentPiece.moveDown()
                } else {
                    val currentPiecePointsInfo = currentPiece.getPoints().map {
                        hashMapOf("point" to it, "color" to currentPiece.color)
                    }
                    busyPoints.addAll(currentPiecePointsInfo)
                    currentPiece.getPoints().forEach {(row, column) ->
                        validPositionsBoard[row][column] = false
                    }
                    val scoredRows = getScoredRows()
                    runOnUiThread {
                        if (scoredRows.size > 0) {
                            score += (scoredRows.size * 100).toLong()
                            binding.scoreValue.text = score.toString()
                        }
                        scoredRows.sort()
                        scoredRows.forEach {
                            destroyRow(it)
                        }
                    }
                    val nextPiece = getRandomPiece()
                    if (piecePositionIsValid(nextPiece)) {
                        currentPiece = nextPiece
                    } else {
                        val settings = getSharedPreferences("${R.string.app_name}_settings", Context.MODE_PRIVATE)
                        val highestScore = settings.getLong("highest_score", 0)
                        val settingsEditor = settings.edit()
                        val secondHighest =  settings.getLong("second_highest_score", 0)
                        val thirdHighest =  settings.getLong("third_highest_score", 0)
                        val recordWasBeated = score > highestScore
                        when {
                            score > highestScore -> {
                                settingsEditor.putLong("highest_score", score).apply()
                                settingsEditor.putLong("second_highest", highestScore).apply()
                                settingsEditor.putLong("third_highest", secondHighest).apply()
                            }
                            score > secondHighest -> {
                                settingsEditor.putLong("second_highest", score).apply()
                                settingsEditor.putLong("third_highest", secondHighest).apply()
                            }
                            score > thirdHighest -> {
                                settingsEditor.putLong("third_highest", score).apply()
                            }
                        }
                        isRunning = false
                        val resultIntent = Intent(this, ResultActivity::class.java)
                        resultIntent.putExtra("score", score)
                        resultIntent.putExtra("record_was_beated", recordWasBeated)
                        startActivity(resultIntent)
                        finish()
                    }
                }
            }
        }.start()
    }
}
