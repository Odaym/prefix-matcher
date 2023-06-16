import sys

from datetime import datetime
from trie import Trie

words = open('/usr/share/dict/words').read().splitlines()
words = words * 10

trie = Trie()

for w in words:
    trie.insert(w)

print('Total words: ', len(words))

start = datetime.now()
result = trie.starts_with(sys.argv[1])
end = datetime.now()
print('With Trie: ', (end - start).microseconds / 1000)

start = datetime.now()
result = [x for x in words if x.startswith(sys.argv[1])]
end = datetime.now()
print('With StartsWith: ', (end - start).microseconds / 1000)


