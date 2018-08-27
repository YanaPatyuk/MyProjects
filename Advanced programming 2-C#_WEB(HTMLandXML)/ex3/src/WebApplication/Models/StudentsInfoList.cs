using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Web;
using System.Web.Hosting;
using System.Web.Mvc;

namespace WebApplication.Models
{
    public class StudentsInfoList
    {
        private List<Student> students;
        public List<Student> Students
        {
            get { return students; }
        }
        public StudentsInfoList()
        {
            students = new List<Student>();
            String line;
            try
            {
                //Pass the file path and file name to the StreamReader constructor
                TextReader sr = new StreamReader(System.Web.Hosting.HostingEnvironment.MapPath("~/App_Data/Info.txt"));
                //Continue to read until you reach end of file
                while ((line = sr.ReadLine()) != null)
                {
                    string[] studentData = line.Split(' ');
                    Student s = new Student
                    {
                        LastName = studentData[1],
                        IDNumber = studentData[2],
                        FirstName = studentData[0]
                    };
                    if (!students.Contains(s))
                        students.Add(s);
                }
                //close the file
                sr.Close();
            }
            catch (Exception e)
            {
                Console.WriteLine("Exception: " + e.Message);
            }
        }
    }
}