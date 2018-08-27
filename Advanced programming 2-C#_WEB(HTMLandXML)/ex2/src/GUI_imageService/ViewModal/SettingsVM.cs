using Comunication.Event;
using GUI_imageService;
using GUI_imageService.Modal;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Data;

namespace Image_Service_GUI.ViewModel
{
    class SettingsVM
    {
        /// <summary>
        /// Members
        /// </summary>
        private string outputDic;
        private string srcName;
        private string logName;
        private string thumbnailSize;
        private Boolean removeEnabled;
        private SettingsModel settings;
        // holds all the current handled directoreis - directories under tracking
        private ObservableCollection<HandlerDirectories> handlerDirsList;
        // events to trigger when "AddToList/NotifyDirRemove" is needed. (outside class triggers them)
        public event EventHandler<ServerDataReciecedEventArgs> NotifyDirRemove;
        public event EventHandler<PropertyChangedEventArgs> AddToList;

        /// <summary>
        /// Properties
        /// </summary>
        public ObservableCollection<HandlerDirectories> HandlerDirsList { get; set; }
        public string VM_OutputDirectory
        {
            get
            {
                return outputDic;

            }
            set
            {
                outputDic = value;
            }
        }
        public string VM_SrcName
        {
            get
            {
                return srcName;

            }
            set { srcName = value; }
        }
        public string VM_LogName
        {
            get
            {
                return logName;

            }
            set { logName = value; }
        }
        public string VM_ThumbnailSize
        {
            get
            {
                return thumbnailSize;

            }
            set { thumbnailSize = value; }
        }
        public Boolean VM_RemoveEnabled
        {
            get
            {
                return this.removeEnabled;

            }
        }


        public SettingsVM()
        {
            this.settings = new SettingsModel();
            this.HandlerDirsList = new ObservableCollection<HandlerDirectories>();
            // in order to always sync HandlerDirsList (make sure bind works)
            BindingOperations.EnableCollectionSynchronization(HandlerDirsList, HandlerDirsList);
            this.settings.ReadSettingsFromServer += SetSettingsData; //listen to server to get settings or handled to close
            NotifyDirRemove += this.settings.RemoveHandler; // add delegates to do on NotifyDirRemove event
            AddToList += AddHandlerToList; // add delegates to do on AddToList event
            this.settings.GetSettings();
            System.Threading.Thread.Sleep(300); // in order to give the GUI some time to bind the data
            this.removeEnabled = false; // Disable Remove button unless some directory is chosen
        }

        /// <summary>
        /// reads setting data from server, and sets them to the prop's
        /// </summary>
        /// <param name="obj"></param>
        /// <param name="e"></param>
        public void SetSettingsData(object obj, ServerDataReciecedEventArgs e)
        {
            if (e.DataType.Equals("Settings")) // if reads "settings" as pre-command from server, we now need to read settings
            {
                JObject settingsObj = JObject.Parse(e.Date);
                string handlers = (string)settingsObj["Handler"];
                string[] handlerList = handlers.Split(';');
                foreach (string handler in handlerList)
                {
                    this.AddToList(this, new PropertyChangedEventArgs(handler));
                }
                this.thumbnailSize = "" + (int)settingsObj["thumbNail"];
                this.logName = (string)settingsObj["logName"];
                this.srcName = (string)settingsObj["sourceName"];
                this.outputDic = (string)settingsObj["OutPutDir"];
            }
            // if not Log or Close cmd & not settings.. - Handlers cmd
            else if (e.DataType.Equals("Log") && e.Date.StartsWith("0:close handler:"))
            {
                string starts = e.Date.Substring(16); // reads handlres...
                HandlerDirectories dir = null;
                foreach (HandlerDirectories dirr in this.HandlerDirsList)
                {
                    if (dirr.Path.Equals(starts))
                    {
                        dir = dirr;
                    }
                }
                if (dir != null)
                {
                    this.HandlerDirsList.Remove(dir);
                    this.removeEnabled = false;
                }
            }
        }

        /// <summary>
        /// Notify Server about Removal of dir.
        /// </summary>
        /// <param name="obj"></param>
        /// <param name="e"></param>
        public void NotifyServerRemove(object obj, PropertyChangedEventArgs e)
        {
            NotifyDirRemove(this, new ServerDataReciecedEventArgs("Close handler", e.PropertyName));
        }

        /// <summary>
        /// Add Handler directory to list
        /// </summary>
        /// <param name="obj"></param>
        /// <param name="path">of dir to add to list</param>
        private void AddHandlerToList(object obj, PropertyChangedEventArgs path)
        {
            HandlerDirectories dir = new HandlerDirectories() { Path = path.PropertyName };
            this.HandlerDirsList.Add(dir);
        }

        /// <summary>
        /// Remove Handler directory from list
        /// </summary>
        /// <param name="obj"></param>
        /// <param name="path">of dir to remove from list</param>
        private void RemoveHandler(object obj, PropertyChangedEventArgs path)
        {
            HandlerDirectories dir = new HandlerDirectories() { Path = path.PropertyName };
            this.HandlerDirsList.Remove(dir);
            Console.WriteLine("remove handelrsss: path:prop:" + path.PropertyName);
        }
    }
}
