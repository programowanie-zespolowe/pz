using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace WhereToGoEntities.WhereToGo.Models
{
    public partial class Authentications
    {
        [DatabaseGeneratedAttribute(DatabaseGeneratedOption.Identity)]
        [Key, Column("IdAuthentication", Order = 1)]
        public int IdAuthentication { get; set; }
        public string NameDevice { get; set; }
        public string MacId { get; set; }
        public bool? IsBlocked { get; set; }
    }
}
