using System;
using System.Collections.Generic;

namespace WhereToGoEntities.WhereToGo.Models
{
    public partial class Buildings
    {
        public Buildings()
        {
            BuildingImages = new HashSet<BuildingImages>();
        }

        public int IdBuilding { get; set; }
        public int? IdUser { get; set; }
        public string NameBuilding { get; set; }
        public byte[] ImageBuilding { get; set; }

        public virtual Users IdUserNavigation { get; set; }
        public virtual ICollection<BuildingImages> BuildingImages { get; set; }
    }
}
