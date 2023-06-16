class TrieRayNode<Key : Any>(var key: Key?, var parent: TrieRayNode<Key>?) {

    val children: HashMap<Key, TrieRayNode<Key>> = HashMap()

    var isTerminating = false
}

class TrieRay<Key : Any> {

    private val root = TrieRayNode<Key>(key = null, parent = null)

    fun insert(list: List<Key>) {
        var current = root

        list.forEach { element ->
            if (current.children[element] == null) {
                current.children[element] = TrieRayNode(element, current)
            }
            current = current.children[element]!!
        }

        current.isTerminating = true
    }

    fun contains(list: List<Key>): Boolean {
        var current = root

        list.forEach { element ->
            val child = current.children[element] ?: return false
            current = child
        }

        return current.isTerminating
    }

    fun remove(list: List<Key>) {
        var current = root

        list.forEach { element ->
            val child = current.children[element] ?: return
            current = child
        }

        if (!current.isTerminating) return

        current.isTerminating = false

        while (current.parent != null && current.children.isEmpty() && !current.isTerminating) {
            current.parent!!.children.remove(current.key)
            current = current.parent!!
        }
    }

    fun collections(
        prefix: List<Key>
    ): List<List<Key>> {
        var current = root

        prefix.forEach { element ->
            val child = current.children[element] ?: return emptyList()
            current = child
        }

        return collections(prefix, current)
    }

    private fun collections(
        prefix: List<Key>,
        node: TrieRayNode<Key>?,
    ): List<List<Key>> {
        val results = mutableListOf<List<Key>>()

        if (node?.isTerminating == true) {
            results.add(prefix)
        }

        node?.children?.forEach { (key, node) ->
            results.addAll(collections(prefix + key, node))
        }

        return results
    }
}


fun TrieRay<Char>.insert(string: String) {
    insert(string.toList())
}

fun TrieRay<Char>.allMatches(prefix: String): List<String> {
    return collections(prefix.toList()).map {
        it.joinToString(separator = "")
    }
}
