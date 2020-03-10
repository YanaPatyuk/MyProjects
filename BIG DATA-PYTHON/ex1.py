import requests 
import lxml.html
import random
import time
import urllib.robotparser
import requests
import lxml.etree
#list of urls that we can crawl

#Base node for url
class url_node:
	def __init__(self, full_url, father_url, rank):
		self.full_url = full_url
		self.father_url = father_url
		self.rank = rank
		self.is_crawled = False
		
#add to given url the full path of wiki 
def _addURLofWiki(url_end):
	return "https://en.wikipedia.org" + url_end
	
	
#calculate the url rank. chack if exist in list 
def _claculateRank(url_n, listOfNodes):
	#if father is none means-first node
	if url_n.father_url is None:
		if __isKingOrQueen(url_n.full_url):#if its king/queen rank 1
			url_n.rank = 1
		else:#any other-0
			url_n.rank = 50
	else:
		if url_n.father_url.rank < 50:#if the father if king/queen or reletive to king/queen
			url_n.rank = url_n.father_url.rank + 1
		if __isKingOrQueen(url_n.full_url):#if its a king or queen rank as first
			url_n.rank = 1		
	
	
def __isKingOrQueen(path):
	#if path conatin king/queen means he was/is queen/king/
	if 'king' in path or 'King' in path or 'queen' in path or 'Queen' in path or '_I' in path or '_VI' in path:
			return True
	if path.endswith('V'):
		return True
	return False


#return min rank node that we didnt crawled and if can crawl or not
def getNodeToCrawl( current, list_nodes):
	#get first node to srart with-thar is not crawled
	current_node = list_nodes[0]
	for n in list_nodes:
		if not n.is_crawled:
			current_node = n
			break
	#find next node not crawked with best rank.
	for node in list_nodes:
		if current_node.rank >= node.rank and not node.is_crawled:
			current_node = node
	#if all list is crawled or list is empty
	if current_node.is_crawled or len(list_nodes) == 0:
		return False,current_node
	return True,current_node
	
	
#add node to list
def _addToLisr(node, list):
	#check if node in the list-if does-update him if needed
	for t in list:
		if t.full_url == node.full_url:
			if t.father_url.full_url == node.father_url.full_url:
				return
			if t.rank > node.rank:#update current node to higher place
				t.rank = node.rank
				t.father_url = node.father_url
				return
	list.append(node)
	 
	

# url_Father - to start crawling from
# xpaths - 
# test with urls = crawl("https://en.wikipedia.org/wiki/Elizabeth_II", [])
#listOfLists-list of crawled pages

def _crawl(rp, url_Father, xpaths,listOfLists,counter):
	res = requests.get(url_Father.full_url) 
	doc = lxml.html.fromstring(res.content)
	i=0	
	url_Father.is_crawled = True
	
	# XPath expression: seek links of royal family
	for xpath in xpaths:
		#for each link 
		for urlPath in doc.xpath(xpath):
			#if link outside wiki-next
			if not urlPath.startswith("/wiki"):
				continue
			#add full path
			current_url= _addURLofWiki(urlPath)
			current_node = url_node(current_url, url_Father, 50)
			#get the rank
			_claculateRank(current_node, listOfLists) 
			#add to list
			_addToLisr(current_node, listOfLists)
	#if 50 urls crawled-return
	counter=counter+1
	if counter == 50:
		return listOfLists
	#upsate url-is crawled	
	canCrawl = True
	while canCrawl:
			# Choose random URL from the discovered ones-min rank
			canCrawl,next_node = getNodeToCrawl( url_Father,listOfLists)
			if not canCrawl:
				break;
			try:
				if rp.can_fetch("*",next_node.full_url):
					time.sleep(1)
					return _crawl(rp,next_node, xpaths, listOfLists,counter)
				else:
				#problem to crawl-dont crawl
					next_node.is_crawled = True
			except:
				# error in crawling URL, dont crawl
				next_node.is_crawled = True	
				pass
	return listOfLists

def crawl(url,xpaths):
	#This lib used for reading from robots.txt and check url's ethics
	rp = urllib.robotparser.RobotFileParser()
	rp.set_url("https://en.wikipedia.org/robots.txt")
	rp.read()
	rrate = rp.request_rate("*")
	counter = 0#counter to know how many times we crawled
	first_url = url_node(url,None,50)#create first url
	_claculateRank(first_url,[])#give it a rank
	#add first url and send to _crawl
	urls_crawled_list =[]
	urls_crawled_list.append(first_url)
	urls_crawled_list = _crawl(rp,first_url,xpaths,urls_crawled_list,counter)
	#ceate list of solutions
	solution_list =[]
	for node in urls_crawled_list:
		if node.father_url is None:
			continue
		solution_list.append([node.father_url.full_url, node.full_url])
	return solution_list

"""
this part for debugging only
origin_url = "https://en.wikipedia.org/wiki/Elizabeth_II"
origin_url ="https://en.wikipedia.org/wiki/George_VI"
#origin_url ="https://en.wikipedia.org/wiki/Edward_VIII"
page = requests.get(origin_url)
doc = lxml.etree.fromstring(page.content)
xpath_list = [
			 "//table[@class='infobox vcard']//th[contains(text(), 'Father') or contains(text(), 'Mother')]/..//@href[contains(.,'/wiki/')]",
			  "//p[contains(text(), 'sibling') or contains(text(), 'sister') or contains(text(), 'brother')]/a[contains(text(), 'Prince')]//@href[contains(.,'/wiki/')]",
			  "//table[@class='infobox vcard']//th[contains(text(), 'Spouse')]/..//@href[contains(.,'/wiki/')]",
			  "//table[@class='infobox vcard']//th[contains(text(), 'Issue')]//..//@href[contains(.,'/wiki/')]",
			  "//table[@class ='wikitable plainrowheaders' or @class ='wikitable']//td[4]//@href[contains(.,'/wiki/')]",
			  "//table[@class ='wikitable plainrowheaders' or @class ='wikitable']//td[5]//@href[contains(.,'/wiki/')]",
			  "//table[@class ='wikitable plainrowheaders' or @class ='wikitable']//td[6]//@href[contains(.,'/wiki/')]"]

my_list = crawl("https://en.wikipedia.org/wiki/Elizabeth_II", xpath_list)
print("\n")
for t in my_list:
	print(t)

"""