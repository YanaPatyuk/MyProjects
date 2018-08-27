using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebApplication.Models
{
    public class PhotoInfo
    {
        public string Path { get; }
        public string TimeTaken { get; }
        public string Name { get; }
        public string RelativePath { get; }

        /// <summary>
        /// Generate photoInfo object using path, timeTaken and name, relative path automaticly created
        /// </summary>
        /// <param name="path"></param>
        /// <param name="timeTaken"></param>
        /// <param name="name"></param>
        public PhotoInfo (string path, string timeTaken, string name)
        {
            this.Path = path;
            this.TimeTaken = timeTaken;
            this.Name = name;
            this.RelativePath = AbsToRelativePath(path);
        }

        /// <summary>
        /// Generate photoInfo object using path, timeTaken,name and relative path
        /// </summary>
        /// <param name="path"></param>
        /// <param name="timeTaken"></param>
        /// <param name="name"></param>
        /// <param name="reletivePath"></param>
        public PhotoInfo(string path, string timeTaken, string name, string reletivePath)
        {
            this.Path = path;
            this.TimeTaken = timeTaken;
            this.Name = name;
            this.RelativePath = reletivePath;
        }

        /// <summary>
        /// Converts given full path to relative one
        /// </summary>
        /// <param name="path"></param>
        /// <returns></returns>
        public string AbsToRelativePath(string path)
        {
            string[] relativePath = path.Split('\\');
            return @"~\" + relativePath[relativePath.Length - 6] + '\\'  +  relativePath[relativePath.Length - 5] + '\\' + relativePath[relativePath.Length - 4] + '\\' +
                 relativePath[relativePath.Length - 3] + '\\' + relativePath[relativePath.Length - 2] + '\\' +
                relativePath[relativePath.Length - 1];
        }

    }
}