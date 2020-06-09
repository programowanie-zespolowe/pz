using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace LinuxWS.Models
{
    public class MdlOutdoorGameRecord
    {
        public int IdRecord { get; set; }
        public int? IdOutdoorGame { get; set; }
        public string Mac { get; set; }
        public string Name { get; set; }
        public DateTime? StartDate { get; set; }
        public DateTime? EndDate { get; set; }
        public int Time { get; set; }
    }
}
