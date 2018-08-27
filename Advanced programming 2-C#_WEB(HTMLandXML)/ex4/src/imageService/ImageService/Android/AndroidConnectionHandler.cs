using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace ImageService.Android
{
  public  class AndroidConnectionHandler
    {
        private int port;
        private AndroidHandler handler;
        private TcpListener listener;



        #region Properties
        #endregion
        /// <summary>
        /// C'tor
        /// </summary>
        /// <param name="port"number></param>
        /// <param name="ch">client handler</param>
        public AndroidConnectionHandler(string directory)
        {
            this.port = 8005;
            this.handler = new AndroidHandler(directory);
        }
        /// <summary>
        /// connect server with port and ip and start listening for new clients
        ///for each client, create delegete that will be listening for incomme logs.
        /// </summary>
        public void Start()
        {
            IPEndPoint ep = new IPEndPoint(IPAddress.Any, port);
            listener = new TcpListener(ep);
            listener.Start();
            //start listen to clients
            Task task = new Task(() => {
                while (true)
                {
                    try
                    {
                        TcpClient client = listener.AcceptTcpClient();
                        handler.HandleClient(client);
                        //create delegete that will listent to new info from server to client and send.
                    }
                    catch (SocketException)//for any problem meanse server disconnected
                    {
                        break;
                    }
                }
            });
            task.Start();
        }
        public void Stop()
        {
            listener.Stop();
        }
    



    }
}
