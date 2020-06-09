using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;
using linuxWS_Configure.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Options;
using Microsoft.IdentityModel.Tokens;
using WhereToGoEntities.WhereToGo.Models;

namespace linuxWS_Configure.Controllers
{
    [Route("Admin/[controller]")]
    [ApiController]
    public class LoginController : ControllerBase
    {
        static string key { get; set; } = "21232f297a57a5a743894a0e4a801fc3";
        private readonly AuthOptions _authOptions;
        public LoginController(IOptions<AuthOptions> authOptionsAccessor)
        {
            _authOptions = authOptionsAccessor.Value;
        }

        [HttpPost]
        public IActionResult Post([FromBody] Users users)
        {

            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            using WhereToGoContext whereToGo = new WhereToGoContext();
            var item = whereToGo.Users.Where(i => i.Name == users.Name).FirstOrDefault();
            UserSettings.IdAdmin = item.IdUser;
            if (item != null)
            {
                try
                {
                    if (Decrypt(users.Password).Equals(Decrypt(item.Password)))
                    {
                        var authClaims = new[]
                      {
                        new Claim(JwtRegisteredClaimNames.Sub, users.Name),
                        new Claim(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString())
                    };

                        var token = new JwtSecurityToken(
                            issuer: _authOptions.Issuer,
                            audience: _authOptions.Audience,
                            expires: DateTime.Now.AddHours(_authOptions.ExpiresInMinutes),
                            claims: authClaims,
                            signingCredentials: new SigningCredentials(new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_authOptions.SecureKey)),
                                SecurityAlgorithms.HmacSha256Signature)
                            );

                        return Ok(new
                        {
                            token = new JwtSecurityTokenHandler().WriteToken(token),
                            expiration = token.ValidTo
                        });
                    }
                    else
                    {
                        return Unauthorized();
                    }
                }
                catch
                {
                    return Unauthorized();
                }

            }
            else
            {
                return Unauthorized();
            }
        }
        private static string Decrypt(string cipher)
        {
            using (var md5 = new MD5CryptoServiceProvider())
            {
                using (var tdes = new TripleDESCryptoServiceProvider())
                {
                    tdes.Key = md5.ComputeHash(UTF8Encoding.UTF8.GetBytes(key));
                    tdes.Mode = CipherMode.ECB;
                    tdes.Padding = PaddingMode.PKCS7;

                    using (var transform = tdes.CreateDecryptor())
                    {
                        byte[] cipherBytes = Convert.FromBase64String(cipher);
                        byte[] bytes = transform.TransformFinalBlock(cipherBytes, 0, cipherBytes.Length);
                        return UTF8Encoding.UTF8.GetString(bytes);
                    }
                }
            }
        }
    }
}