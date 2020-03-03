using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace linuxWS.Models
{
    public class MdlBuildings
    {
        public int IdBuilding { get; set; }
        public int? IdUser { get; set; }
        public string NameBuilding { get; set; }
        public byte[] ImageBuilding { get; set; }   
    }
}

