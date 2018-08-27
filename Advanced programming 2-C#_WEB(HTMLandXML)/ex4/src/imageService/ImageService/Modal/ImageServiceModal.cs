using Infrastructure;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Imaging;
using System.IO;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading;
using System.Threading.Tasks;

namespace ImageService.Modal
{
    public class ImageServiceModal : IImageServiceModal
    {
        #region Members
        private string m_OutputFolder;            // The Output Folder
        private int m_thumbnailSize;              // The Size Of The Thumbnail Size
        // one time initialization in order to save resources when the function uses it called again. (grabage collector)
        private static Regex r = new Regex(":");
        #endregion

        public ImageServiceModal(string outputDir, int thumbnailSize)
        {
            this.m_OutputFolder = outputDir;
            this.m_thumbnailSize = thumbnailSize;
        }

        /// <summary>
        /// The Function Addes A file to the system
        /// </summary>
        /// <param name="path">The Path of the Image</param>
        /// <returns>Indication if the Addition Was Successful (updates the resulut bool) 
        /// and an *error message* / output folder of image path 
        /// (if you use it it will open the folder where the image sits)</returns>
        public string AddFile(string path, out bool result)
        {
            // file doesn't exist
            if (!File.Exists(path))
            {
                System.Threading.Thread.Sleep(10000);
                if (!File.Exists(path))
                {
                    result = false;
                    return "File doesn't Exsist";
                }
            }
            // file exist, we need to find out photo "taken time" or at least creation time (which anyfile should have)
            DateTime date = new DateTime();
            try
            {
                date = GetDateTakenFromImage(path); // try to retrieve taken time
            }
            catch
            {
                try
                {
                    date = File.GetCreationTime(path); // try to retrieve created time
                }
                catch (IOException e)
                {
                    result = false;
                    return "Couldn't retrieve photo date" + e.Message;
                }
            }

            // create output directory and find target path
            try
            {
                // create output directory (if doesn't doesn't exist already)
                DirectoryInfo di = Directory.CreateDirectory(m_OutputFolder);
                //if the folder isnt hidden-flag it hide
                if ((di.Attributes & FileAttributes.Hidden) != FileAttributes.Hidden)
                    di.Attributes |= FileAttributes.Hidden;
                // create thumbnail directory (if doesn't doesn't exist already)
                string thumbnailsPath = m_OutputFolder + "\\" + "Thumbnails";
                Directory.CreateDirectory(thumbnailsPath);

                // find out year and month
                int year = date.Year;
                int month = date.Month;
                string yearAndMonthPath = "\\" + year.ToString() + "\\" + month.ToString();
                string thumbnailsYearAndMonthPath = thumbnailsPath + yearAndMonthPath;

                // create subfolders
                Directory.CreateDirectory(m_OutputFolder + yearAndMonthPath);
                DirectoryInfo diThunb = Directory.CreateDirectory(thumbnailsPath + yearAndMonthPath);

                string fullTargetPath = m_OutputFolder + "\\" + year.ToString() + "\\" + month.ToString() + "\\";
                string fullThumbnailsTargetPath = thumbnailsPath + "\\" + year.ToString() + "\\" + month.ToString() + "\\";

                // find out file name, and extention
                string fileNameWithoutExtention = Path.GetFileNameWithoutExtension(path);
                string fileExtention = Path.GetExtension(path);
                string fullFileName = fileNameWithoutExtention + fileExtention;

                // moves the file "as is" if not exist already, otherwise add 1,2,3,...n extention
                // calc extention if needed
                int extentionCounter = 1;
                while (true)
                {
                    // stop counting when extention isn't in use
                    if (File.Exists(fullTargetPath + fullFileName))
                    {
                        string[] parts = fullFileName.Split('.');
                        fullFileName = fileNameWithoutExtention + " (" + extentionCounter + ")." + parts[1];
                        extentionCounter++;
                    }
                    else
                    {
                        break;
                    }
                }

                try
                {
                    // moves the file (it doesn't exist if reached here)
                    File.Move(path, fullTargetPath + fullFileName);
                }
                catch (IOException e)//if the file is in use of other thread-wait and try again.
                {
                    System.Threading.Thread.Sleep(500);
                    File.Move(path, fullTargetPath + fullFileName);
                }
                // create thumbnail if not exist already
                if (!File.Exists(fullThumbnailsTargetPath + fullFileName))
                {
                    using (Image image = Image.FromFile(fullTargetPath + fullFileName))
                    using (Image thumbnail = image.GetThumbnailImage(this.m_thumbnailSize, this.m_thumbnailSize, () => false, IntPtr.Zero))
                    {
                        thumbnail.Save(fullThumbnailsTargetPath + fullFileName);
                    }
                }

                // return output folder of image path (if you use it it will open the folder where the image sits)
                result = true;
                return m_OutputFolder + "\\" + year.ToString() + "\\" + month.ToString();
            }
            catch (IOException e)
            {
                result = false;
                return "Something went wrong while creating or moving the file" + e.Message;
            }
        }

        /// <summary>
        /// retrieves the taken time of image WITHOUT loading the whole image (way faster) @ stackoverflow
        /// </summary>
        public static DateTime GetDateTakenFromImage(string path)
        {
            using (FileStream fs = new FileStream(path, FileMode.Open, FileAccess.Read))
            using (Image myImage = Image.FromStream(fs, false, false))
            {
                PropertyItem propItem = myImage.GetPropertyItem(36867);
                string dateTaken = r.Replace(Encoding.UTF8.GetString(propItem.Value), "-", 2);
                return DateTime.Parse(dateTaken);
            }
        }
    }
}
