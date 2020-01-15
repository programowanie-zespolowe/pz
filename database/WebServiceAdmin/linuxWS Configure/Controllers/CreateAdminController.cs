using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using WhereToGoEntities.WhereToGo.Models;

namespace linuxWS_Configure.Controllers
{
    [Authorize]
    [ApiController]
    [Route("Admin/[controller]")]
    public class CreateAdminController : ControllerBase
    {
        private Users mdlUsers;
        static string key { get; set; } = "21232f297a57a5a743894a0e4a801fc3";
        [HttpPost]
        public IActionResult Post([FromBody] Users users)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            using WhereToGoContext whereToGo = new WhereToGoContext();
            var item = whereToGo.Users.Where(i => i.Name == users.Name).FirstOrDefault();

            if (item != null)
            {
                return BadRequest("Konto o takiej nazwie juz jest");
            }
            else
            {
                mdlUsers = new Users();
                mdlUsers.Name = users.Name;
                mdlUsers.Password = Encrypt(users.Password);
                mdlUsers.Email = users.Email;
                whereToGo.Users.Add(mdlUsers);
                whereToGo.SaveChanges();
                return Ok("Utworzono konto");
            }

        }
        private static string Encrypt(string text)
        {
            using (var md5 = new MD5CryptoServiceProvider())
            {
                using (var tdes = new TripleDESCryptoServiceProvider())
                {
                    tdes.Key = md5.ComputeHash(UTF8Encoding.UTF8.GetBytes(key));
                    tdes.Mode = CipherMode.ECB;
                    tdes.Padding = PaddingMode.PKCS7;

                    using (var transform = tdes.CreateEncryptor())
                    {
                        byte[] textBytes = UTF8Encoding.UTF8.GetBytes(text);
                        byte[] bytes = transform.TransformFinalBlock(textBytes, 0, textBytes.Length);
                        return Convert.ToBase64String(bytes, 0, bytes.Length);
                    }
                }
            }
        }
    }
}