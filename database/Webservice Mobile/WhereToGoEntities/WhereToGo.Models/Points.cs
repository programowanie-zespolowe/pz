using System;
using System.Collections.Generic;

namespace WhereToGoEntities.WhereToGo.Models
{
    public partial class Points
    {
        public int IdPoint { get; set; }
        public int? IdImage { get; set; }
        public string NamePoint { get; set; }
        public double? X { get; set; }
        public double? Y { get; set; }
        public int? IdPointType { get; set; }

        public virtual BuildingImages IdImageNavigation { get; set; }
        public virtual PointType IdPointTypeNavigation { get; set; }
    }
}
