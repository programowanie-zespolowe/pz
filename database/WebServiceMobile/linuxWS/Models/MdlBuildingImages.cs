using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace linuxWS.Models
{
    public class MdlBuildingImages
    {
        public int IdImage { get; set; }
        public int? IdBuilding { get; set; }
        public byte[] PathImage { get; set; }
        public int? BuildingLevel { get; set; }
        public double? NorthPointAngle { get; set; }

    }
}
