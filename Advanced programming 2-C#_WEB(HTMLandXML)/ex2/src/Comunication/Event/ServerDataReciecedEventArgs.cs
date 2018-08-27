using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Comunication.Event
{
    public class ServerDataReciecedEventArgs: EventArgs
    {
        public string DataType { get; set; }
        public string Date { get; set; }
        /// <summary>
        /// C'tor for ServerDataReciecedEventArgs.
        /// </summary>
        /// <param name="type"> string of name</param>
        /// <param name="info">data from server that relevent</param>
        public ServerDataReciecedEventArgs(string type, string info)
        {
            this.DataType = type;
            this.Date = info;
        }

        /// <summary>
        /// convart to json.
        /// </summary>
        /// <returns>string in json version for info</returns>
        public string ToJSON()
        {
            JObject settingsObj = new JObject();
            settingsObj["type"] = this.DataType;
            settingsObj["data"] = this.Date;
            return settingsObj.ToString();
        }
        /// <summary>
        /// create ServerDataReciecedEventArgs from json string.
        /// </summary>
        /// <param name="str"></param>
        /// <returns></returns>
        public static ServerDataReciecedEventArgs FromJSON(string str)
        {
            JObject settingsObj = JObject.Parse(str);
            ServerDataReciecedEventArgs info = new ServerDataReciecedEventArgs(
                                                (string)settingsObj["type"], (string)settingsObj["data"]);
            return info;
        }
    }
}
