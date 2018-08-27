using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ImageService.Modal
{
    public interface IImageServiceModal
    {
        /// <summary>
        /// The Function Addes A file to the system
        /// </summary>
        /// <param name="path">The Path of the Image from the file</param>
        /// <returns>Indication if the Addition Was Successful (updates the resulut bool) 
        /// and an *error message* / output folder of image path 
        /// (if you use it it will open the folder where the image sits)</returns>
        string AddFile(string path, out bool result);
    }
}
