


from cloud import *

def breakcloud(cloud):
	"""
	receives 'cloud', an object of type Cloud.
	creates a file with the name 'plain.txt' that stores the current text that is encrypted in the cloud.
	you can use only the Read/Write interfaces of Cloud (do not use its internal variables.)
	"""

	file= open("plain.txt","w+")
	length = cloud.Length()
	tampPlainText = '\x00'  # some input for our choice.
# for each byte in the cloulde
	for i in range(length):
		# replace in position the tamp plain text input.
		# note:we save the realCrypted to decrype later
		realCrypted = cloud.Write(i,tampPlainText)
		# read the crypted byte of tampPlainText
		tampEncrypted = cloud.Read(i)
		# xor the pl with the pt gives us the spesific byre in the key
		key = ''.join(chr(ord(a) ^ ord(b)) for a,b in zip(tampPlainText , tampEncrypted))
		# use the key to xor with the realCripted to get the plainText from the cloud
		realPlainText =''.join(chr(ord(a) ^ ord(b)) for a, b in zip(key, realCrypted))
		file.write(realPlainText)
	file.close()

