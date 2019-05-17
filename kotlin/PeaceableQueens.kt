import java.time.LocalDateTime

val O = 'O'
val B = 'B'
val W = 'W'
val PRINT_FREQ = 100000

fun main(args: Array<String>) {
	val n = args[0].toInt()
	val length = n*n
	val startTime = System.currentTimeMillis()
	val solutions = mutableMapOf<String, Int>()	
	val startingBoard = O.toString().repeat(length)
	var board = startingBoard
	var count = 0
	
	do {
		count++
		val numB = board.count { it == B }
		val numW = board.count { it == W }
		val valid = numB == numW && numB > 0
		
		if (count % PRINT_FREQ == 0) {
			println(board)
		}
		
		if (valid) {
			val solution = checkSolution(board, n)
			if (solution) {
				solutions.put(board, numB)
			}
		}

		board = incrementBoard(board)
	} while (board != startingBoard)
	val endTime = System.currentTimeMillis()
	
	val runtimeMillis = endTime - startTime
	val runtimeMinutes = runtimeMillis/1000.0/60
	println("running time $runtimeMillis ms, ($runtimeMinutes min)")
	println("$count boards checked")
	println("${solutions.size} solutions found")
	val bestSolution = solutions.toList().sortedBy { (_, value) -> value }.last()
	println("best solution\n$bestSolution")
	println(solutions)
}

fun incrementBoard(board:String): String {
	val list = board.toMutableList()
	var index = list.lastIndex;
	
	do {
		val old = list.get(index)
		val new = incrementDigit(old)
		list.set(index, new)
		index--
	} while (new == O && index >=0)

	return list.joinToString(separator = "")
}

fun incrementDigit(digit: Char): Char =  
	when(digit) {
		O -> B
		B -> W
		W -> O
		else -> throw Exception("invalid digit in board: " + digit)
	}

fun checkSolution(board: String, n: Int): Boolean =
	rowSolution(board, n) && columnSolution(board, n) && diagonalSolution(board, n)

fun rowSolution(board: String, n: Int): Boolean {
	val rows = buildRows(board, n)
	rows.forEach {
		if (it.contains(B) && it.contains(W)) {
			return false
		}
	}
	
	return true
}

fun columnSolution(board: String, n: Int): Boolean {
	val columns = buildColumns(board, n)
	
	columns.forEach {
		if (it.contains(B) && it.contains(W)) {
			return false
		}
	}
	
	return true
}

fun diagonalSolution(board: String, n: Int): Boolean {
	val diagonals = buildDiagonals(board, n)

	diagonals.forEach {
		if (it.contains(B) && it.contains(W)) {
			return false
		}
	}
	
	return true
}

fun buildRows(board: String, n: Int) = board.chunked(n)

fun buildColumns(board: String, n: Int): List<List<Char>> {
	val rows = buildRows(board, n)
	val columns: MutableList<MutableList<Char>> = mutableListOf()
	
	for (i in 0..n-1) {
		val column = mutableListOf<Char>()

		for (j in 0..n-1) {
			val row = rows.get(j)
			column.add(row.get(i))
		}
		
		columns.add(column)
	}
	
	return columns
}

fun buildDiagonals(board: String, n: Int): List<List<Char>> {
	val columns = buildColumns(board, n)
	val diagonals: MutableList<List<Char>> = mutableListOf()
	val downRightStart = Point(0,0)
	val downLeftStart = Point(n-1,0)
	
	// downRight 
	for (i in 0..n-1) {
		diagonals.add(buildDownRight(columns, Point(downRightStart.x, downRightStart.y+i), n))
		diagonals.add(buildDownRight(columns, Point(downRightStart.x+i, downRightStart.y), n))
	}

	// downLeft
	for (i in 0..n-1) {
		diagonals.add(buildDownLeft(columns, Point(downLeftStart.x-i, downLeftStart.y), n))
		diagonals.add(buildDownLeft(columns, Point(downLeftStart.x, downLeftStart.y+i), n))
	}
	
	return diagonals
}

fun buildDownRight(columns: List<List<Char>>, startingPoint: Point, n: Int): List<Char> {
	var currentPoint = startingPoint
	val diagonal = mutableListOf<Char>()

	while (currentPoint.x < n && currentPoint.y < n) {
		diagonal.add(columns.get(currentPoint.x).get(currentPoint.y))
		currentPoint = Point(currentPoint.x + 1, currentPoint.y + 1)
	}
	
	return diagonal
}

fun buildDownLeft(columns: List<List<Char>>, startingPoint: Point, n: Int): List<Char> {
	var currentPoint = startingPoint
	val diagonal = mutableListOf<Char>()

	while (currentPoint.y < n && currentPoint.x >= 0) {
		diagonal.add(columns.get(currentPoint.x).get(currentPoint.y))
		currentPoint = Point(currentPoint.x - 1, currentPoint.y + 1)
	}
	
	return diagonal
}

data class Point(val x: Int, val y: Int)