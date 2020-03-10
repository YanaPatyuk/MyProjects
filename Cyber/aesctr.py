from Crypto import Random
from Crypto.Util import Counter
import Crypto.Cipher.AES as AES


for fname in ['what.txt', 'where.txt', 'why.txt']:
	with open(fname, mode='rb') as file:
		pt = file.read()
		file.close()
		
		nonce = Random.get_random_bytes(8)
		countf = Counter.new(64, nonce)
		key = Random.get_random_bytes(32)
		

		crypto = AES.new(key, AES.MODE_CTR, counter=countf)
		ct = crypto.encrypt(pt)
		

		print " ".join("{:02x}".format(ord(c)) for c in pt)
		print " ".join("{:02x}".format(ord(c)) for c in countf())
		print " ".join("{:02x}".format(ord(c)) for c in key)
		print " ".join("{:02x}".format(ord(c)) for c in ct)
		print "\n\n"