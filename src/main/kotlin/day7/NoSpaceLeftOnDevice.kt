package day7

import util.Solution

sealed interface TerminalInteraction
sealed interface FileDescriptor : TerminalInteraction
data class File(val name: String, val size: Int) : FileDescriptor
data class Dir(val name: String) : FileDescriptor
data class ChangeDirectoryCommand(val to: Dir) : TerminalInteraction

val fileRegex = """(\d+) (.*)""".toRegex()

class NoSpaceLeftOnDevice(fileName: String) : Solution<TerminalInteraction, Long>(fileName) {
    override fun parse(line: String): TerminalInteraction? {
        val matchResult = fileRegex.matchEntire(line)
        return when {
            line.startsWith("$ cd ") -> ChangeDirectoryCommand(Dir(line.drop(5)))
            line == "$ ls" -> null
            line.startsWith("dir ") -> Dir(line.drop(4))
            matchResult != null -> File(matchResult.groupValues[2], matchResult.groupValues[1].toInt())
            else -> null
        }
    }

    override fun solve1(data: List<TerminalInteraction>): Long {
        tailrec fun listDirectories(
            interactions: List<TerminalInteraction>,
            current: Map<Dir, List<FileDescriptor>> = emptyMap(),
            listing: List<FileDescriptor> = emptyList(),
            currentDir: Dir? = null
        ): Map<Dir, List<FileDescriptor>> {
            return if (interactions.isEmpty()) current + (currentDir!! to listing) else {
                val head = interactions.first()
                val tail = interactions.drop(1)
                when (head) {
                    is FileDescriptor -> listDirectories(tail, current, listing + head, currentDir)
                    is ChangeDirectoryCommand ->
                        if (currentDir == null || currentDir.name == "..") {
                            listDirectories(tail, current, emptyList(), head.to)
                        } else {
                            listDirectories(tail, current + (currentDir to listing), emptyList(), head.to)
                        }
                }
            }
        }


        val dirs = listDirectories(data)

        fun calculateDirectorySizes(directories: List<Dir>, currentSizes: Map<Dir, Long>): Map<Dir, Long> {
            return if (directories.isEmpty()) currentSizes else {
                val head = directories.first()
                val tail = directories.drop(1)
                if (head in currentSizes) calculateDirectorySizes(tail, currentSizes) else {
                    val children = dirs[head]!!
                    val fileSizes = children.sumOf {
                        when (it) {
                            is File -> it.size
                            else -> 0
                        }
                    }
                    
                    val dirSizes = children.sumOf { 
                        when (it) {
                            is Dir -> calculateDirectorySizes(listOf(it), currentSizes)[it]!!
                            else -> 0
                        }
                    }
                    calculateDirectorySizes(tail, currentSizes + (head to fileSizes + dirSizes))
                }
            }
        }

        val sizes: Map<Dir, Long> = calculateDirectorySizes(dirs.keys.toList(), emptyMap())
        
        return sizes.filterValues { it <= 100000 }.values.sum()
    }

    override fun solve2(data: List<TerminalInteraction>): Long {
        TODO("Not yet implemented")
    }

}