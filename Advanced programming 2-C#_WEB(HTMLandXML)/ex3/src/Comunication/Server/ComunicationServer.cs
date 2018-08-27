using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;


using System.Configuration;
using System.IO;
using Comunication.Server;
using Logging.Modal;
using Comunication.Event;

namespace Comunication.Server
{
    /// <summary>
    /// this class is tcp server
    /// </summary>
   public class ComunicationServer
    {
        private int port;
        private TcpListener listener;
        private IClientHandler ch;

        #region Properties
        public event EventHandler<MessageRecievedEventArgs> GUICommandRecieved;          // The event that notifies about a new Command being recieved
        #endregion
        /// <summary>
        /// C'tor
        /// </summary>
        /// <param name="port"number></param>
        /// <param name="ch">client handler</param>
        public ComunicationServer(int port, IClientHandler ch)
        {
            this.port = port;
            this.ch = ch;
        }
        /// <summary>
        /// connect server with port and ip and start listening for new clients
        ///for each client, create delegete that will be listening for incomme logs.
        /// </summary>
        public void Start()
        {
            IPEndPoint ep = new IPEndPoint(IPAddress.Parse("127.0.0.1"), port);
            listener = new TcpListener(ep);
            listener.Start();
            //start listen to clients
            Task task = new Task(() => {
                while (true)
                {
                    try
                    {
                        TcpClient client = listener.AcceptTcpClient();
                        ch.HandleClient(client);
                        //create delegete that will listent to new info from server to client and send.
                        EventHandler<MessageRecievedEventArgs> clientLogListener = null;
                        clientLogListener = delegate (object sender, MessageRecievedEventArgs e)
                        {
                            //send to client orders
                            try
                            {
                                NetworkStream stream = client.GetStream();
                                BinaryWriter writer = new BinaryWriter(stream);
                                {
                                    ServerDataReciecedEventArgs message = new ServerDataReciecedEventArgs("Log", (int)e.Status + ":" + e.Message);
                                    writer.Write(message.ToJSON());
                                }
                                //if send-error:stop listen
                            } catch(Exception)
                            {
                                GUICommandRecieved -= clientLogListener;
                                client.Close();
                            }
                        };
                        GUICommandRecieved += clientLogListener;
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
        /// <summary>
        /// listener event-for each new message for clients-notify all clients to send.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        public void SendNewLog(object sender, MessageRecievedEventArgs e)
        {
            try
            {
                GUICommandRecieved(this, e);
            } catch(Exception)//if no client connected
            {
                return;
            }
        }
    }
}
