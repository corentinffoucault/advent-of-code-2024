data class Point2D(val x: Int, val y: Int) {

    companion object {
        fun fromString(x: String, y: String): Point2D {
                return Point2D(x.toInt(), y.toInt())
        }
    }

    operator fun plus(other: Point2D): Point2D =
        Point2D(x + other.x, y + other.y)

    operator fun minus(other: Point2D): Point2D =
        Point2D(x - other.x, y - other.y)

    operator fun times(times: Int): Point2D =
        Point2D(x * times, y * times)

    override fun toString(): String {
        return "$x $y"
    }
}