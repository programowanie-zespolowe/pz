using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WhereToGo.Admin.Models
{
    public class MdlPointsConnection
    {
        public int IdPointConnection { get; set; }
        public int? IdPointStart { get; set; }
        public int? IdPointEnd { get; set; }
    }
}
