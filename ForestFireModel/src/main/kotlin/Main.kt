fun main(args: Array<String>) {
    println("Welcome to the FFM! 🔥")
    println("Program arguments: ${args.joinToString()}")
    ForestFire().run(args[0].toInt(), args[1].toInt())
}