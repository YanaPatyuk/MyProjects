using Comunication.Client;
using Comunication.Event;
using Infrastructure.Enums;
using Logging.Modal;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GUI_imageService.Modal
{
    class LogModal
    {
        public event EventHandler<LogRecord> NewLogNotify; // event to trigger when new logs arrive

        /// <summary>
        /// C'tor for logModal
        /// </summary>
        public LogModal()
        {
            GuiClient c = GuiClient.Instance;
            c.Connect();
            c.ServerMassages += ReadFromServer; // add methods to trigger when new message arive to GUI client
        }

        /// <summary>
        /// GetLogList cmd to send to server when asking for log list
        /// </summary>
        public void GetLogList()
        {
            if (GuiClient.Instance.ConnectedToServer)
            {
                GuiClient.Instance.SendMessage("" + (int)CommandEnum.LogCommand);
            }
        }

        /// <summary>
        /// The function being called when new data recived. it check what data type it is
        /// if its a log list or only one log, add to the list. any other case ignore.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        public void ReadFromServer(object sender, ServerDataReciecedEventArgs e)
        {
            //if only one log recived
            if (e.DataType.Equals("Log") && (!(e.Date.StartsWith("0:close handler:"))))
            {
                string[] logRecord = e.Date.Split(':');
                NewLogNotify(this, new LogRecord((MessageTypeEnum)int.Parse(logRecord[0]), e.Date));
            }
            //if log list(!) recived. recived in string, each log sperated by ";"
            else if (e.DataType.Equals("LogList"))
            {
                //make an array from the logs and add them.
                string[] logList = e.Date.Split(';');
                foreach (string log in logList)
                {
                    string[] logRecord = log.Split(':');
                    NewLogNotify(this, new LogRecord((MessageTypeEnum)int.Parse(logRecord[0]), log)); // reads each log separately
                }
            }
        }
    }
}
