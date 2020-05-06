import sys
import subprocess
from bs4 import BeautifulSoup


def main(fname):

    with open(fname, 'r') as f:
        for line in f:
            data = f.readline()
            name = data.split('.')

            if(name[3] == "ham"):
                path = "./enron6/ham/"
            else:
                path = "./enron6/spam/"
            path = path + data
            path = path.rstrip()

            with open(path, 'r') as g:
                text = g.read().lower().replace(':','').replace('\n',' ').split(' ')
                g.close()

            with open("ustopwords.txt", 'r') as h:
                stopwords = h.read().split('\n')
                h.close()

            totalStopwords = 0
            for i in range(0,len(text)):
                for j in range(0, len(stopwords)):
                    if(text[i] == stopwords[j]):
                        totalStopwords = totalStopwords + 1

            print(data.rstrip('\n') + ":" ,totalStopwords)
if __name__ == "__main__":
    main(sys.argv[1])