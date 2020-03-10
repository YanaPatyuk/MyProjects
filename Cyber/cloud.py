

# Last modified: 7 May 2019

# you don't have to use the packages below, it is only a suggestion. 
# do not use any other python module unless it is explicitly permitted by the instructor.
from Crypto import Random
from Crypto.Util import Counter
import Crypto.Cipher.AES as AES


class Cloud:
	"""
	the cloud stores your content encrypted.
	you can add variables and methods to this class as you want.
	"""
	def __init__(self, filename, key=Random.get_random_bytes(32), nonce=Random.get_random_bytes(8)):
		"""
		Encrypt the content of 'filename' and store its ciphertext at self.ciphertext
		The encryption to use here is AES-CTR with 32 byte key.
		The counter should begin with zero.
		"""
		self.fileName = filename
		self.key = key
		self.nonce = nonce
		self.ciphertext = None
		with open(self.fileName, mode='rb') as file:
			plainText = file.read()
			file.close()
			countf = Counter.new(64, self.nonce)
			crypto = AES.new(self.key, AES.MODE_CTR, counter=countf)
			# use a list for the encrypted text for better byte access
			self.ciphertext = list(crypto.encrypt(plainText))


	def Length(self):
		"""
		Returns the length of the plaintext/ciphertext (they are of the same length).
		This is necessary so one would not read/write with an invalid position.
		"""
		return len(self.ciphertext)

	def Read(self, position=0):
		"""
		Returns one byte at 'position' from current self.ciphertext. 
		position=0 returns the first byte of the ciphertext.
		"""
		return self.ciphertext[position]

	def Write(self, position=0, newbyte='\x33'):
		"""
		Replace the byte in 'position' from self.ciphertext with the encryption of 'newbyte'.
		Remember that you should encrypt 'newbyte' under the appropriate key (it depends on the position).
		Return the previous byte from self.ciphertext (before the re-write).
		"""

		prevEncrypt = self.ciphertext[position]
		# create a string of 1's in the length of the ciphertext-place all in list
		tamp = list('1'*len(self.ciphertext))
		# change the in the given position the value to the new given newbyte
		tamp[position] = newbyte
		""" 
		encrypte the string. because the string is in the same length,
		the same key-byte that encrypted this position in the original text, will now encrypt the new byte.
		replace to new crypted given byte.
		"""
		countf = Counter.new(64, self.nonce)
		crypto = AES.new(self.key, AES.MODE_CTR, counter=countf)
		self.ciphertext[position] = list(crypto.encrypt("".join(tamp)))[position]
		return prevEncrypt


