using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WhereToGo.Admin.Models
{
    public class MdlOutdoorGameHints
    {
        public int IdHints { get; set; }
        public int? IdOutdoorGame { get; set; }
        public int? IdPoint { get; set; }
        public string Hint { get; set; }
    }
}
