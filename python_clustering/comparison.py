"""
	comparsion.py
	Author: Matthew Christian
	Compares two texts and returns the similiarity score
	based on the number of common words
"""
import nltk
import nltk.stem
import pprint
import string
from nltk.stem.snowball import SnowballStemmer

articles = [
	open('cnn_article.txt', 'r').read(),
	open('bbc_article.txt', 'r').read()
]

text = []
for i, article in enumerate(articles):
	tokens = nltk.word_tokenize(article)

	stemmer = SnowballStemmer("english")
	stemmed_tokens = [stemmer.stem(token) for token in tokens]

	nopunc = []
	for t in stemmed_tokens:
		if t[0] not in string.punctuation:
			nopunc.append(t)
		else:
			nopunc.append(None)

	text.append(nopunc)

	z = [(t[0], t[1], t[2]) for t in zip(tokens, stemmed_tokens, nopunc)]
	print('Article', i, 'Tokens')
	pprint.pprint(z)

score = 0
total = 0
for w in text[0]:
	if not w:
		continue
	if w in text[1]:
		score += 1
	total += 1

score = score / total

print('Score', score)
