using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Image_Service_GUI.ViewModel
{
    public class HandlerDirectories : INotifyPropertyChanged
    {
        private string path;
        public string Path
        {
            get { return this.path; }
            set
            {
                if (this.path != value)
                {
                    this.path = value;
                    this.NotifyPropertyChanged("Path");
                }
            }
        }
        public event PropertyChangedEventHandler PropertyChanged; // event to trigger when path changes
        // method to trigger when folder path changed [not in use for now]
        public void NotifyPropertyChanged(string propName)
        {
            if (this.PropertyChanged != null)
                this.PropertyChanged(this, new PropertyChangedEventArgs(propName));
        }
    }
}
