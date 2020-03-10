import requests
import lxml.etree


origin_url = "https://en.wikipedia.org/wiki/Elizabeth_II"
origin_url1 ="https://en.wikipedia.org/wiki/George_VI"
origin_url2 ="https://en.wikipedia.org/wiki/Edward_VIII"


page = requests.get(origin_url)
doc = lxml.etree.fromstring(page.content)
"""
xpath_list = [
			  "//table[@class='infobox vcard']//th[contains(text(), 'Father')]/..//a[contains(@href, 'https')]",
			  "//table[@class='infobox vcard']//th[contains(text(), 'Mother')]/..//@href",
			  "//p[contains(text(), 'sibling') or contains(text(), 'sister' or contains(text(), 'brother')]/a[contains(text(), 'Prince')]//@href",
			   "//table[@class='infobox vcard']//th[contains(text(), 'Spouse')]/..//@href",
			  "//table[@class='infobox vcard']//th[contains(text(), 'Issue')]//..//@href",
			  "//table[@class ='wikitable plainrowheaders' or @class='wikitable ']//td[1]//@href",
			  "//table[@class ='wikitable plainrowheaders' or @class ='wikitable']//td[4]//@href",
			  "//table[@class ='wikitable plainrowheaders' or @class ='wikitable']//td[5]//@href",
			  "//table[@class ='wikitable plainrowheaders' or @class ='wikitable']//td[6]//@href"]"""
xpath_list = [
			 "//table[@class='infobox vcard']//th[contains(text(), 'Father') or contains(text(), 'Mother')]/..//@href[contains(.,'/wiki/')]",
			  "//p[contains(text(), 'sibling') or contains(text(), 'sister') or contains(text(), 'brother')]/a[contains(text(), 'Prince')]//@href[contains(.,'/wiki/')]",
			  "//table[@class='infobox vcard']//th[contains(text(), 'Spouse')]/..//@href[contains(.,'/wiki/')]",
			  "//table[@class='infobox vcard']//th[contains(text(), 'Issue')]//..//@href[contains(.,'/wiki/')]",
			  "//table[@class ='wikitable plainrowheaders' or @class ='wikitable']//td[4]//@href[contains(.,'/wiki/')]",
			  "//table[@class ='wikitable plainrowheaders' or @class ='wikitable']//td[5]//@href[contains(.,'/wiki/')]",
			  "//table[@class ='wikitable plainrowheaders' or @class ='wikitable']//td[6]//@href[contains(.,'/wiki/')]"]


text_file = open("Output.txt", "w")
	# write the answers to a file, just for testing.
for xpath in xpath_list:
	print(xpath)
	s =xpath+"\n"
	text_file.write(str(s))
	t = "Example: given "+ origin_url +" the XPath returns\n"
	text_file.write(str(t))
	for url in doc.xpath(xpath):
		text_file.write(url)
		text_file.write("\n")
		print(url)
	text_file.write("\n")
	print("\n")
page = requests.get(origin_url1)
doc = lxml.etree.fromstring(page.content)
for xpath in xpath_list:
	print(xpath)
	s =xpath+"\n"
	text_file.write(str(s))
	t = "Example: given"+ origin_url1 +" the XPath returns\n"
	text_file.write(str(t))
	for url in doc.xpath(xpath):
		text_file.write(url)
		text_file.write("\n")
		print(url)
	print("\n")
	text_file.write("\n")
text_file.close()