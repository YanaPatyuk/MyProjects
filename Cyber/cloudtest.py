

# Last modified: 7 May 2019



from cloud import *
from breakcloud import *

tests = list()

tests.append({"filename":"what.txt",
			 "key":b'\x2b\x7e\x15\x16\x28\xae\xd2\xa6\xab\xf7\x15\x88\x09\xcf\x4f\x3c\x2b\x7e\x15\x16\x28\xae\xd2\xa6\xab\xf7\x15\x88\x09\xcf\x4f\x3c',
			 "nonce":b'\x00\x00\x00\x00\x00\x00\x00\x00',
			 "ops":[('r',(10,'\xa1')), ('r', (20,'\x32')), ('w', (5,'\x10','\x29')), ('r', (5,'\x50'))]})

tests.append({"filename":"why.txt",
			  "key":b'\x11\x11\x11\x11\x28\xae\xd2\xa6\xab\xf7\x15\x88\x09\xcf\x4f\x3c\x2b\x7e\x15\x16\x28\xae\xd2\xa6\xab\xf7\x15\x88\x09\xcf\x4f\x3c',
			  "nonce": b'\xcf\xdd\x02\x45\x67\x89\x00\x01',
			  "ops":[('r',(15,'\x39')), ('r',(20, '\x6e')), ('w', (34,'\xcf', '\xf7')), ('r', (34, '\x59')) ]})

tests.append({"filename":"where.txt",
			  "key":b'\x11\x11\x11\x11\x28\xae\xd2\xa6\xab\xf7\x15\x88\x09\xcf\x4f\x3c\x2b\x7e\x15\x16\x28\xae\xd2\xa6\xab\xf7\x15\x88\x09\xcf\x4f\x3c',
			  "nonce": b'\xcf\xdd\x02\x45\x67\x89\x00\x01',
			  "ops":[('r',(100,'\x5b')), ('r',(120, '\x6f')), ('w', (45,'\xcf', '\x16')), ('r', (45, '\xbb')) ]})


def testcloud():
	for test in tests:
		cloud = Cloud(test["filename"], test["key"], test["nonce"])
		for i in range(len(test["ops"])):
			op,desc = test["ops"][i]
			if op == 'r':
				pos,expected = desc
				res = cloud.Read(pos)
				if not expected == res:
					print "!! error: reading position %d, expected \\x%s but received \\x%s "%(pos,"{:02x}".format(ord(expected)),"{:02x}".format(ord(res)))
					print "failed..."
					exit(1)
				print "good: reading position %d, expected=res= \\x%s"%(pos,"{:02x}".format(ord(expected)))
			else:
				pos,value,expected = desc
				res = cloud.Write(pos, value)
				if not expected == res:
					print "!! error: writing value \\x%s to position %d, expected \\x%s but received \\x%s "%("{:02x}".format(ord(value)),pos,"{:02x}".format(ord(expected)),"{:02x}".format(ord(res)))
					print "failed..."
					exit(1)
				print "good: position %d, expected=res= \\x%s"%(pos,"{:02x}".format(ord(expected)))
	print "success!"

def testbreaker():
	testfile = 'what.txt'
	cloud = Cloud(testfile)
	breakcloud(cloud)

	with open(testfile, mode='rb') as file:
		originalcontent = file.read()
		file.close()
		with open('plain.txt', mode='rb') as breakerresult:
			breakercontent = breakerresult.read()
			breakerresult.close()

			if originalcontent == breakercontent:
				print "success!"
				exit()
	print "failed..."


def main():
	testcloud()
	# testbreaker()


if __name__ == '__main__':
	main()



