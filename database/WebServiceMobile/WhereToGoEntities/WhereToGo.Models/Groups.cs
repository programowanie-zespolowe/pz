using System;
using System.Collections.Generic;

namespace WhereToGoEntities.WhereToGo.Models
{
    public partial class Groups
    {
        public Groups()
        {
            Points = new HashSet<Points>();
        }

        public int IdGroup { get; set; }
        public string NameGroup { get; set; }
        public byte[] ImageGroup { get; set; }

        public virtual ICollection<Points> Points { get; set; }
    }
}
