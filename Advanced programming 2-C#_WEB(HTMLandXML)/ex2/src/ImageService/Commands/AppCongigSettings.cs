using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ImageService.Commands
{
    /// <summary>
    /// this class is singelton/ used for saving all settings from appconfig file.
    /// </summary>
    class AppCongigSettings
    {
        private static AppCongigSettings instance;

        private AppCongigSettings() { }

        public static AppCongigSettings Instance
        {
            get
            {
                if (instance == null)
                {
                    instance = new AppCongigSettings();
                }
                return instance;
            }
        }
    
        public string OutPutDir { get; set; }
        public string SourceName { get; set; }
        public string LogName { get; set; }
        public int ThumbNail { get; set; }
        public string Handlers { get; set; }
        /// <summary>
        /// convat settings to JSON
        /// </summary>
        /// <returns>string in json format</returns>
        public string ToJSON()
        {
            JObject settingsObj = new JObject();
            settingsObj["OutPutDir"] = OutPutDir;
            settingsObj["sourceName"] = SourceName;
            settingsObj["logName"] = LogName;
            settingsObj["thumbNail"] = ThumbNail;
            settingsObj["Handler"] = Handlers;
            return settingsObj.ToString();
        }
        /// <summary>
        /// convart string to settings using json
        /// </summary>
        /// <param name="str"></param>
        /// <returns>AppCongigSettings</returns>
        public static AppCongigSettings FromJSON(string str)
        {
            AppCongigSettings settings = new AppCongigSettings();
            JObject settingsObj = JObject.Parse(str);
            settings.Handlers = (string)settingsObj["Handler"];
            settings.ThumbNail = (int)settingsObj["thumbNail"];
            settings.LogName = (string)settingsObj["logName"];
            settings.SourceName = (string)settingsObj["sourceName"];
            settings.OutPutDir = (string)settingsObj["OutPutDir"];
            return settings;
        }

    }
}
