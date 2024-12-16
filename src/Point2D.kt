data class Point2D(val x: Int, val y: Int) {

    operator fun plus(other: Point2D): Point2D =
        Point2D(x + other.x, y + other.y)

    operator fun minus(other: Point2D): Point2D =
        Point2D(x - other.x, y - other.y)

    override fun toString(): String {
        return "$x $y"
    }
}