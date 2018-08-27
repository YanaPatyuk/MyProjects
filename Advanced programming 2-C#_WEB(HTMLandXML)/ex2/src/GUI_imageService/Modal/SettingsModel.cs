using Comunication.Client;
using Comunication.Event;
using Infrastructure.Enums;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GUI_imageService.Modal
{
    class SettingsModel
    {
        public List<string> Handlers { get; private set; }
        // event to triger when new settings received from server
        public event EventHandler<ServerDataReciecedEventArgs> ReadSettingsFromServer;
        public SettingsModel()
        {
            this.Handlers = new List<string>();
            GuiClient.Instance.Connect();
            GuiClient.Instance.ServerMassages += ReadFromServer; // add methods to do when new method recevied form server
        }

        /// <summary>
        /// Sends get settings cmd to server
        /// </summary>
        public void GetSettings()
        {
            if (GuiClient.Instance.ConnectedToServer)
            {
                GuiClient.Instance.SendMessage("" + (int)CommandEnum.GetConfigCommand);
            }
        }

        /// <summary>
        ///  Triggers ReadSettingsFromServer the event each time new message recevied from server
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        public void ReadFromServer(object sender, ServerDataReciecedEventArgs e)
        {
            ReadSettingsFromServer(this, e);
        }
        /// <summary>
        /// sends message "remove handler" to server
        /// </summary>
        /// <param name="obj"></param>
        /// <param name="e"></param>
        public void RemoveHandler(object obj, ServerDataReciecedEventArgs e)
        {
            GuiClient.Instance.SendMessage("" + (int)CommandEnum.CloseCommand + ":" + e.Date);
        }

    }
}
