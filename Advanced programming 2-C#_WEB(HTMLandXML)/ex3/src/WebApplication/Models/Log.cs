using Logging.Modal;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace WebApplication.Models
{
    public class Log
    {
        [Required]
        [DataType(DataType.Text)]
        [Display(Name = "Type")]
        public MessageTypeEnum Type { get; set; }

        [Required]
        [DataType(DataType.Text)]
        [Display(Name = "Data")]
        public string Data {
            get { return this.data; }
            set { this.data = value.Replace(@"\", @"\\"); }
        }
        private string data;

        private void changeType(string path)
        {
            string tamp = path.Replace(@"\\", @"\");
        }
    }
}