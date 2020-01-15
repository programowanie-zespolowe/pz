﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace linuxWS_Configure.Models
{
    public class MdlPoints
    {
        public int IdPoint { get; set; }
        public int? IdImage { get; set; }
        public string NamePoint { get; set; }
        public double? X { get; set; }
        public double? Y { get; set; }
        public int? IdPointType { get; set; }
        public int? IdGroup { get; set; }
        public byte[] ImagePoint { get; set; }
    }
}
