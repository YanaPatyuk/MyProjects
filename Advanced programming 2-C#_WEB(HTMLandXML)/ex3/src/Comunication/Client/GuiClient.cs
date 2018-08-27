using Comunication.Event;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace Comunication.Client
{
    /// <summary>
    /// Gui client is singelton type so we can acsses in diffrent plasce in the code.
    /// </summary>
   public class GuiClient
    {
        private Boolean listenToServer;
        private BasicClient basicClient;
        public event EventHandler<ServerDataReciecedEventArgs> ServerMassages;
        public Boolean ConnectedToServer { get { return this.listenToServer; } }

        private static GuiClient instance = null;
        public static GuiClient Instance
        {
            get
            {
                if (instance == null)
                {
                    instance = new GuiClient();
                }
                return instance;
            }
        }
        /// <summary>
        /// create basic client
        /// </summary>
        private GuiClient()
        {
            basicClient = new BasicClient(); // ori added
        }
        /// <summary>
        /// connect to server.
        /// if already connected-return
        /// </summary>
        public void Connect()
        {
            if (listenToServer != true)
            {
                basicClient.Ep = new IPEndPoint(IPAddress.Parse("127.0.0.1"), 8000);
                basicClient.Client = new TcpClient();
                try
                {
                    listenToServer = basicClient.ConnectToServer();
                    StartListenToServer();

                }
                catch (Exception e)
                {
                    // couldn't connet to server
                    //No connection to server, what a shame =(
                    listenToServer = false;
                }
            }

        }
        /// <summary>
        /// this methode create a task witch always listen to new messages from server.
        /// when new message recived it notify all methodes(settings and logs)
        /// </summary>
        public void StartListenToServer()
        {
            Task task = new Task(() =>
            {
                while(this.listenToServer)//if no connection to server-stop
                {//the try catch is if server disconnected
                    try
                    { 
                    string data = basicClient.ReadDataFromServer();
                    ServerMassages(this, ServerDataReciecedEventArgs.FromJSON(data));
                    }
                    catch (Exception)
                    {
                        ServerMassages(this, new ServerDataReciecedEventArgs("Log", "2:Disconnected from server-bye"));
                        this.listenToServer = false;
                        Disconnect();
                        break;
                    }
                }

            });
            task.Start();
        }
        /// <summary>
        /// disconnect from server
        /// </summary>
        public void Disconnect()
        {
            basicClient.CloseClient();
        }
        /// <summary>
        /// send massage creates new task to send to server reqests from client
        /// note:we use mutex to prevent to reqests to send together messages.
        /// </summary>
        /// <param name="message"></param>
        public void SendMessage(string message)
        {
            Task task = new Task(() =>
            {
                try
                {
                    //Sets the stream write into it
                    basicClient.SendDataToServer(message);
                }
                catch (Exception ex)
                {
                    Console.WriteLine(ex.Message);
                }

            });
            task.Start();
        }

    }
}
