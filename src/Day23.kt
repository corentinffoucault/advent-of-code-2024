import java.io.BufferedReader
import java.io.File

data class Connection(val c1: String, val c2: String) {
    override fun toString(): String {
        return "$c1-$c2"
    }
}

data class LANBy3(val c1: String, val c2: String, val c3: String) {
    override fun toString(): String {
        return "$c1,$c2,$c3"
    }
}

data class LAN(val computers: Set<Computer23>) {
    override fun toString(): String {
        return computers.toList().sortedBy { it.toString() }.joinToString(",")
    }

    fun size(): Int {
        return computers.size
    }
}

data class Computer23(val name: String) {

    private val computers = mutableSetOf<Computer23>()

    override fun toString(): String {
        return name
    }

    fun addComputer(computer: Computer23) {
        computers.add(computer)
    }

    fun intersectValue(entry: Set<Computer23>): Set<Computer23>  {
        return entry intersect computers
    }

    fun values(): Set<Computer23>  {
        return computers
    }

    fun size(): Int {
        return computers.size
    }
}

class Network {

    private val connections = mutableSetOf<Connection>()
    private val seen = mutableSetOf<Connection>()
    private val lans = mutableSetOf<LANBy3>()
    private val computers = mutableMapOf<String, Computer23>()

    override fun toString(): String {
        return connections.toString()
    }

    fun add(connection: String) {
        val computer = connection.split("-").sorted()
        connections.add(Connection(computer[0], computer[1]))
        addComputerLink(computer[0], computer[1])
    }

    private fun addComputerLink(from: String, to: String) {
        if (!computers.contains(from)) {
            computers[from] = Computer23(from)
        }
        if (!computers.contains(to)) {
            computers[to] = Computer23(to)
        }
        computers[from]!!.addComputer(computers[to]!!)
        computers[to]!!.addComputer(computers[from]!!)
    }

    fun getLansByBronkKerbosch(): LAN {
        return bronKerbosch(computers.values.toSet())
    }

    fun bronKerbosch(p: Set<Computer23>, r: Set<Computer23> = emptySet()): LAN {

        if (p.isEmpty()) {
            return LAN(r)
        }
        else {
            val withMostNeighbors: Computer23 = p.maxBy { it.size() }
            return p.minus(withMostNeighbors.values()).map { v ->
                 val a = bronKerbosch(
                    v.intersectValue(p),
                    r + v,
                )
                return@map a
            }.maxBy { it.size() }
        }


        /*
         bronKerbosch(p: Set<Computer23>, r: Set<Computer23>)
             si P est vides alors
                retourner R
             pour tout sommet v dans P faire
                BronKerbosch1(R ⋃ {v}, P ⋂ N(v))
                P := P \ {v}

         BronKerbosch1(∅, V, ∅) => appel initial
         N(v) => voisin de v
         */
    }

    fun getLansBy3(): Set<LANBy3> {
        connections.forEachIndexed { index, connection ->
            getLansFromOneConnection(connection, connections.drop(index + 1))
        }
        return lans
    }

    private fun getLansFromOneConnection(connection: Connection, connections: List<Connection>) {
        connections.forEach { connectionBis ->
            val computers = mutableListOf<String>()
            var valueToMemorize = ""
            if (connection.c1 == connectionBis.c1) {
                computers.add(connection.c2)
                computers.add(connectionBis.c2)
                valueToMemorize = connectionBis.c2
            } else if (connection.c1 == connectionBis.c2) {
                computers.add(connection.c2)
                computers.add(connectionBis.c1)
                valueToMemorize = connectionBis.c1
            } else if (connection.c2 == connectionBis.c1) {
                computers.add(connection.c1)
                computers.add(connectionBis.c2)
                valueToMemorize = connectionBis.c2
            } else if (connection.c2 == connectionBis.c2) {
                computers.add(connection.c1)
                computers.add(connectionBis.c1)
                valueToMemorize = connectionBis.c1
            }
            if (computers.isNotEmpty()) {
                val sortedComputers = computers.sorted()
                val connectionToFind = Connection(sortedComputers[0], sortedComputers[1])
                if (connections.contains(connectionToFind)) {
                    lans.add(LANBy3(connection.c1, connection.c2, valueToMemorize))
                    seen.add(connectionToFind)
                    seen.add(connectionBis)
                }
            }
        }
    }
}

class Day23 {

    fun run(fileName: String) {
        val network = buildNetwork(fileName)
        val reg = Regex("t[a-z]")
        val result = network.getLansBy3().fold(0L) { acc, it ->
            println(it)
            acc + if (reg.containsMatchIn(it.toString())) 1 else 0
        }
        println(result)
        println(network.getLansByBronkKerbosch())

    }

    private fun buildNetwork(fileName: String): Network {
        val network = Network()
        val bufferedReader: BufferedReader = File(fileName).bufferedReader();
        bufferedReader.use {
            it.forEachLine { line: String ->
                network.add(line)
            }
        }
        return network
    }
}

fun main(args: Array<String>) {
    println("Running the main function");
    Day23().run("resource/day23.txt");
}

// 169 tolow