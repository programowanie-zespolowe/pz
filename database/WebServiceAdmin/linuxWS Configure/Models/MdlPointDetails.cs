using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WhereToGo.Admin.Models
{
    public class MdlPointDetails
    {
        public int IdPointDetails { get; set; }
        public int IdPoint { get; set; }
        public string NamePoint { get; set; }
        public int? IdGroup { get; set; }
        public byte[] ImagePoint { get; set; }
    }
}
