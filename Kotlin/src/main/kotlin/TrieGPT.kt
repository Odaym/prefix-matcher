class TrieGPTNode {
    val children = mutableMapOf<Char, TrieGPTNode>()
    var isTerminating = false
}

class TrieGPT {
    private val root = TrieGPTNode()

    fun insert(word: String) {
        var current = root
        for (char in word) {
            val child = current.children.getOrPut(char) { TrieGPTNode() }
            current = child
        }
        current.isTerminating = true
    }

    fun searchPrefix(prefix: String): List<String> {
        val results = mutableListOf<String>()
        val prefixNode = findPrefixNode(prefix)
        if (prefixNode != null) {
            collectWords(prefix, prefixNode, results)
        }
        return results
    }

    fun countTotalWords(): Int {
        var count = if (root.isTerminating) 1 else 0
        for ((_, childNode) in root.children) {
            count += countWords(childNode)
        }
        return count
    }

    private fun countWords(node: TrieGPTNode): Int {
        var count = if (node.isTerminating) 1 else 0
        for ((_, childNode) in node.children) {
            count += countWords(childNode)
        }
        return count
    }

    private fun findPrefixNode(prefix: String): TrieGPTNode? {
        var current = root
        for (char in prefix) {
            val child = current.children[char] ?: return null
            current = child
        }
        return current
    }

    fun contains(word: String): Boolean {
        var currentNode = root
        for (char in word) {
            val child = currentNode.children[char] ?: return false
            currentNode = child
        }
        return currentNode.isTerminating
    }

    private fun collectWords(
        prefix: String,
        node: TrieGPTNode,
        results: MutableList<String>
    ) {
        if (node.isTerminating) {
            results.add(prefix)
        }
        for ((char, childNode) in node.children) {
            collectWords(prefix + char, childNode, results)
        }
    }
}
