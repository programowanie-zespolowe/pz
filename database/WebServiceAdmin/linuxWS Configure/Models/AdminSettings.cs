using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace linuxWS_Configure.Models
{
    public class UserSettings
    {
        private static int idAdmin = 0;

        public static int IdAdmin
        {
            get { return idAdmin; }
            set { idAdmin = value; }

        }

    }
}
