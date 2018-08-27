using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

using System;
using System.Threading;

namespace ImageService.Android
{
    class AndroidHandler
    {
        private string DirectoryPath;
        public AndroidHandler(string path)
        {
            DirectoryPath = path;
        }
		/**
		*get info from clients.(images).
		**/
        public void HandleClient(TcpClient client)
        {
            //create a task witch will listen to  client command and execute them.
            new Task(() =>
            {
                try
                {
                    NetworkStream stream = client.GetStream();
                    BinaryReader reader = new BinaryReader(stream);
                    BinaryWriter writer = new BinaryWriter(stream);

                    while (true)
                    {
                        // read image name
                        List<Byte> byteList = new List<Byte>();
                        Byte[] b = new Byte[1];
                        // read first byte
                        stream.Read(b, 0, 1);
                        byteList.Add(b[0]);
                        while (stream.DataAvailable) // read rest
                        {
                            stream.Read(b, 0, 1);
                            byteList.Add(b[0]);
                        }
                        String imgName = Path.GetFileNameWithoutExtension(System.Text.Encoding.UTF8.GetString(byteList.ToArray()));

                        if (imgName == "close_connection") { break; }

                        Byte[] imgByte = new Byte[1];
                        imgByte[0] = 1;

                        stream.Write(imgByte, 0, 1); // notify client finished reading the name

                        Byte[] len = new Byte[8];
                        stream.Read(len, 0, 8);
                        long length = BitConverter.ToInt64(len, 0);
                        stream.Write(imgByte, 0, 1); // notify client finished reading the length


                        byte[] imgByteArr = ReadImage(stream, length);


                        stream.Write(imgByte, 0, 1); // notify client finished reading the img

                        File.WriteAllBytes(this.DirectoryPath + "\\" + imgName + ".PNG", imgByteArr);
                    }

                    client.Close();
                }
                catch (Exception)
                {
                    client.Close();
                }

            }).Start();
        }
/**
read all image bytes to one aray byts.
**/
        private byte[] ReadImage(NetworkStream stream, long len)
        {
            int readBytes = 0;
            byte[] bytes = new byte[len];
            while(readBytes < len)
            {
                readBytes += stream.Read(bytes, readBytes, ((int)len - readBytes));
            }
            return bytes;
        }

    /// <summary>
    /// Create picture object from ByteArray
    /// </summary>
    /// <param name="bytesArr"></param>
    /// <returns></returns>
    private Image ByteArrayToImage(byte[] bytesArr)
    {
        MemoryStream memstr = new MemoryStream(bytesArr);
        Image img = Image.FromStream(memstr);
        return img;
    }*/
    }
}
