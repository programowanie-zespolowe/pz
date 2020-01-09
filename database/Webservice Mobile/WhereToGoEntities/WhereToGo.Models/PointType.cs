using System;
using System.Collections.Generic;

namespace WhereToGoEntities.WhereToGo.Models
{
    public partial class PointType
    {
        public PointType()
        {
            Points = new HashSet<Points>();
        }

        public int IdPointType { get; set; }
        public string TypePoint { get; set; }

        public virtual ICollection<Points> Points { get; set; }
    }
}
