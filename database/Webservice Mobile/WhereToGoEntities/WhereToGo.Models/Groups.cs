using System;
using System.Collections.Generic;

namespace WhereToGoEntities.WhereToGo.Models
{
    public partial class Groups
    {
        public Groups()
        {
            PointsDetail = new HashSet<PointsDetail>();
        }

        public int IdGroup { get; set; }
        public string NameGroup { get; set; }
        public byte[] ImageGroup { get; set; }
        public int? IdBuilding { get; set; }

        public virtual Buildings IdBuildingNavigation { get; set; }
        public virtual ICollection<PointsDetail> PointsDetail { get; set; }
    }
}
