using System;
using System.Collections.Generic;

namespace WhereToGoEntities.WhereToGo.Models
{
    public partial class Points
    {
        public Points()
        {
            OutdoorGameHints = new HashSet<OutdoorGameHints>();
            OutdoorGamePath = new HashSet<OutdoorGamePath>();
            PointsConnectionIdPointEndNavigation = new HashSet<PointsConnection>();
            PointsConnectionIdPointStartNavigation = new HashSet<PointsConnection>();
            PointsDetail = new HashSet<PointsDetail>();
        }

        public int IdPoint { get; set; }
        public int? IdImage { get; set; }
        public double? X { get; set; }
        public double? Y { get; set; }
        public int? IdPointType { get; set; }
        public byte[] ImagePoint { get; set; }
        public double? Direction { get; set; }
        public bool? OnOffDirection { get; set; }

        public virtual BuildingImages IdImageNavigation { get; set; }
        public virtual ICollection<OutdoorGameHints> OutdoorGameHints { get; set; }
        public virtual ICollection<OutdoorGamePath> OutdoorGamePath { get; set; }
        public virtual ICollection<PointsConnection> PointsConnectionIdPointEndNavigation { get; set; }
        public virtual ICollection<PointsConnection> PointsConnectionIdPointStartNavigation { get; set; }
        public virtual ICollection<PointsDetail> PointsDetail { get; set; }
    }
}
