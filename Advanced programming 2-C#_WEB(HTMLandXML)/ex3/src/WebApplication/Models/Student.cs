using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace WebApplication.Models
{
    public class Student
    {
        static int count = 0;
        public Student()
        {
            count++;
            ID = count;
           // status = ServiceController().Status == ServiceControllerStatus.Running;
        }
        public void copy(Student std)
        {
            FirstName = std.FirstName;
            LastName = std.LastName;
            IDNumber = std.IDNumber;
        }

        [Required]
        [Display(Name = "ID")]
        public int ID { get; set; }
        [Required]
        [DataType(DataType.Text)]
        [Display(Name = "FirstName")]
        public string FirstName { get; set; }

        [Required]
        [DataType(DataType.Text)]
        [Display(Name = "LastName")]
        public string LastName { get; set; }

        [Required]
        [DataType(DataType.Text)]
        [Display(Name = "IDNumber")]
        public string IDNumber { get; set; }

    }
}