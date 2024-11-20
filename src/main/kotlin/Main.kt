fun main() {
    val str = "Example"
    val encoded = Base16384.encodeToString(str)
    val decoded = Base16384.decodeFromString(encoded)
    println("Original String: $str")
    println("Base16384 Encoded String: $encoded")
    println("Base16384 Decoded String: $decoded")
}