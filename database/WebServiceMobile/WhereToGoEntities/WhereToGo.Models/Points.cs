using System;
using System.Collections.Generic;

namespace WhereToGoEntities.WhereToGo.Models
{
    public partial class Points
    {
        public Points()
        {
            PointsConnectionsIdPointEndNavigation = new HashSet<PointsConnections>();
            PointsConnectionsIdPointStartNavigation = new HashSet<PointsConnections>();
        }

        public int IdPoint { get; set; }
        public int? IdImage { get; set; }
        public string NamePoint { get; set; }
        public double? X { get; set; }
        public double? Y { get; set; }
        public int? IdPointType { get; set; }
        public int? IdGroup { get; set; }
        public byte[] ImagePoint { get; set; }

        public virtual Groups IdGroupNavigation { get; set; }
        public virtual BuildingImages IdImageNavigation { get; set; }
        public virtual PointType IdPointTypeNavigation { get; set; }
        public virtual ICollection<PointsConnections> PointsConnectionsIdPointEndNavigation { get; set; }
        public virtual ICollection<PointsConnections> PointsConnectionsIdPointStartNavigation { get; set; }
    }
}
