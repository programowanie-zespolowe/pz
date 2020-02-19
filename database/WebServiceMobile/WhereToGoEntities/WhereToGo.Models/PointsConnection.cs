using System;
using System.Collections.Generic;

namespace WhereToGoEntities.WhereToGo.Models
{
    public partial class PointsConnection
    {
        public int IdPointConnection { get; set; }
        public int? IdPointStart { get; set; }
        public int? IdPointEnd { get; set; }

        public virtual Points IdPointEndNavigation { get; set; }
        public virtual Points IdPointStartNavigation { get; set; }
    }
}
