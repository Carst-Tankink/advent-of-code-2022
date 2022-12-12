package day7

import util.Solution

sealed interface TerminalInteraction
sealed interface FileDescriptor : TerminalInteraction
data class File(val name: String, val size: Int) : FileDescriptor
data class ChangeDirectoryCommand(val to: String) : TerminalInteraction
data class Dir(val name: String, val contents: List<FileDescriptor>) : FileDescriptor

val fileRegex = """(\d+) (.*)""".toRegex()

class NoSpaceLeftOnDevice(fileName: String) : Solution<TerminalInteraction, Int>(fileName) {
    override fun parse(line: String): TerminalInteraction? {
        val matchResult = fileRegex.matchEntire(line)
        return when {
            line.startsWith("$ cd ") -> ChangeDirectoryCommand(line.drop(5))
            line == "$ ls" -> null
            line.startsWith("dir ") -> null
            matchResult != null -> File(matchResult.groupValues[2], matchResult.groupValues[1].toInt())
            else -> null
        }
    }

    override fun solve1(data: List<TerminalInteraction>): Int {
        assert(data.first() == ChangeDirectoryCommand("/"))
        return getSizes(data).values.sumOf { if (it <= 100000) it else 0 }
    }

    private fun getSizes(data: List<TerminalInteraction>): Map<Dir, Int> {
        fun populateDirectory(
            commands: List<TerminalInteraction>,
            name: String,
            contents: List<FileDescriptor> = emptyList()
        ): Pair<Dir, List<TerminalInteraction>> {
            return if (commands.isEmpty()) Pair(Dir(name, contents), commands) else {
                val head = commands.first()
                val tail = commands.drop(1)
                when (head) {
                    is File -> populateDirectory(tail, name, contents + head)
                    is ChangeDirectoryCommand -> {
                        if (head.to == "..") Pair(Dir(name, contents), tail) else {
                            val (subDir, remaining) = populateDirectory(tail, head.to, emptyList())
                            populateDirectory(remaining, name, contents + subDir)
                        }
                    }

                    else -> TODO("Unknown type $head")
                }
            }
        }


        val (dir, _) = populateDirectory(data.drop(1), "/", emptyList())

        fun calculateDirectorySize(dir: Dir): Map<Dir, Int> {
            val fileSizes = dir.contents.filterIsInstance<File>().sumOf { it.size }

            val subDirs = dir.contents.filterIsInstance<Dir>()
            val dirMaps = subDirs
                .flatMap { calculateDirectorySize(it).entries }
                .associate { Pair(it.key, it.value) }

            val subDirSizes = subDirs.sumOf { dirMaps[it]!! }
            return dirMaps + (dir to fileSizes + subDirSizes)
        }


        return calculateDirectorySize(dir)
    }

    override fun solve2(data: List<TerminalInteraction>): Int {
        val sizes = getSizes(data)
        val rootSize = sizes.entries.find { it.key.name == "/" }!!.value

        val unused = 70_000_000 - rootSize
        val neededToRemove = 30_000_000 - unused

        return sizes.filterValues { it > neededToRemove }.minOf { it.value }
    }

}