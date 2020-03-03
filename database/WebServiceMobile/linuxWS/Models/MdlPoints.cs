using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace linuxWS.Models
{
    public class MdlPoints
    {
        public int IdPoint { get; set; }
        public int? IdImage { get; set; }
        public double? X { get; set; }
        public double? Y { get; set; }
        public int? IdPointType { get; set; }
        public byte[] ImagePoint { get; set; }
    }
}
