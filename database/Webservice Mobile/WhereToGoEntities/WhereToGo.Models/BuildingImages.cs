using System;
using System.Collections.Generic;

namespace WhereToGoEntities.WhereToGo.Models
{
    public partial class BuildingImages
    {
        public BuildingImages()
        {
            Points = new HashSet<Points>();
        }

        public int IdImage { get; set; }
        public int? IdBuilding { get; set; }
        public byte[] PathImage { get; set; }
        public int? BuildingLevel { get; set; }
        public double? Scale { get; set; }
        public double? NorthPointAngle { get; set; }

        public virtual Buildings IdBuildingNavigation { get; set; }
        public virtual ICollection<Points> Points { get; set; }
    }
}
