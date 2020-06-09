using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace LinuxWS.Models
{
    public class MdlOutdoorGame
    {
        public int IdOutdoorGame { get; set; }
        public int? IdBuilding { get; set; }
        public string NameGame { get; set; }
        public byte[] ImageGame { get; set; }
        public DateTime? StartDateGame { get; set; }
        public DateTime? EndDateGame { get; set; }
        public int? IdFirstPoint { get; set; }
    }
}
