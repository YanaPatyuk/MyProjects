using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace Comunication.Client
{
    class BasicClient
    {
        public BasicClient() { }
        private IPEndPoint ep;
        public IPEndPoint Ep { get { return ep; } set { ep = value; } }
        private TcpClient client;
        private NetworkStream stream;
        public TcpClient Client { get { return this.client; } set { client = value; } }
        private static Mutex model_mutex = new Mutex();


        private Boolean Run { get; set; }

        /// <summary>
        /// connect client to basicClient
        /// </summary>
        public Boolean ConnectToServer()
        {
            try
            {
                //try to connect
                this.Client.Connect(Ep);
                stream = this.Client.GetStream();
                this.Run = true;
            }
            catch (Exception)//if connection failed
            {
                this.Run = false;
            }
            return Run;
        }
        /// <summary>
        /// send string message to basicClient
        /// </summary>
        /// <param name="message"></param>
        public void SendDataToServer(string message)
        {
            BinaryWriter writer = new BinaryWriter(stream);
            {
                // Send data to basicClient
                model_mutex.WaitOne();
                writer.Write(message);
                model_mutex.ReleaseMutex();
            }
        }
        /// <summary>
        /// read message-string from basicClient
        /// </summary>
        /// <returns></returns>
        public string ReadDataFromServer()
        {
            string data;
            BinaryReader reader = new BinaryReader(stream);
            {
                // Get data from basicClient
                data = reader.ReadString();
            }
            return data;
        }
        /// <summary>
        /// close the connection to basicClient;
        /// </summary>
        public void CloseClient()
        {
            this.Client.Close();
            this.Run = false;
        }
    }
}
