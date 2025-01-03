data class Point2D(val x: Int, val y: Int) {

    companion object {

        val ORIGIN = Point2D(0, 0)
        val NORTH = Point2D(0, -1)
        val EAST = Point2D(1, 0)
        val SOUTH = Point2D(0, 1)
        val WEST = Point2D(-1, 0)
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

    fun cardinalNeighbors(): Set<Point2D> =
        setOf(
            this + NORTH,
            this + EAST,
            this + SOUTH,
            this + WEST
        )
}