using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace LinuxWS.Models
{
    public class MdlPointDetails
    {
        public int IdPointDetails { get; set; }
        public int IdPoint { get; set; }
        public string NamePoint { get; set; }
        public int? IdGroup { get; set; }
    }
}
