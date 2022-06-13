package me.spste.common.model

data class SimulationResult(val map:List<List<Int>>, val result:List<List<Int>>, val variation: Map<Int,MapUpdate>, val wind: Wind, val climate: Climate)

data class MapUpdate(val pixelUpdateList: List<PixelUpdate>)

data class PixelUpdate(val x: Int, val y: Int, val value: Int)
