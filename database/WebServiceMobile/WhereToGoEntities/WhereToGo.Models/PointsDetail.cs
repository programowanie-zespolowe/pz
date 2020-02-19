using System;
using System.Collections.Generic;

namespace WhereToGoEntities.WhereToGo.Models
{
    public partial class PointsDetail
    {
        public int IdPointDetails { get; set; }
        public int IdPoint { get; set; }
        public string NamePoint { get; set; }
        public int? IdGroup { get; set; }

        public virtual Groups IdGroupNavigation { get; set; }
        public virtual Points IdPointNavigation { get; set; }
    }
}
