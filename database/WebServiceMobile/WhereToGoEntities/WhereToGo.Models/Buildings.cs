using System;
using System.Collections.Generic;

namespace WhereToGoEntities.WhereToGo.Models
{
    public partial class Buildings
    {
        public Buildings()
        {
            BuildingImages = new HashSet<BuildingImages>();
            Groups = new HashSet<Groups>();
            OutdoorGame = new HashSet<OutdoorGame>();
        }

        public int IdBuilding { get; set; }
        public int? IdUser { get; set; }
        public string NameBuilding { get; set; }
        public byte[] ImageBuilding { get; set; }
        public double? Scale { get; set; }

        public virtual Users IdUserNavigation { get; set; }
        public virtual ICollection<BuildingImages> BuildingImages { get; set; }
        public virtual ICollection<Groups> Groups { get; set; }
        public virtual ICollection<OutdoorGame> OutdoorGame { get; set; }
    }
}
