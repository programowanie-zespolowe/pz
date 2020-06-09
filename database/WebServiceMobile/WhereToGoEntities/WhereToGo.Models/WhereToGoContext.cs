using System;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata;

namespace WhereToGoEntities.WhereToGo.Models
{
    public partial class WhereToGoContext : DbContext
    {
        public WhereToGoContext()
        {
        }

        public WhereToGoContext(DbContextOptions<WhereToGoContext> options)
            : base(options)
        {
        }

        public virtual DbSet<Authentications> Authentications { get; set; }
        public virtual DbSet<BuildingImages> BuildingImages { get; set; }
        public virtual DbSet<Buildings> Buildings { get; set; }
        public virtual DbSet<Groups> Groups { get; set; }
        public virtual DbSet<OutdoorGame> OutdoorGame { get; set; }
        public virtual DbSet<OutdoorGameHints> OutdoorGameHints { get; set; }
        public virtual DbSet<OutdoorGamePath> OutdoorGamePath { get; set; }
        public virtual DbSet<OutdoorGameRecordTime> OutdoorGameRecordTime { get; set; }
        public virtual DbSet<PointType> PointType { get; set; }
        public virtual DbSet<Points> Points { get; set; }
        public virtual DbSet<PointsConnection> PointsConnection { get; set; }
        public virtual DbSet<PointsDetail> PointsDetail { get; set; }
        public virtual DbSet<Users> Users { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (!optionsBuilder.IsConfigured)
            {
#warning To protect potentially sensitive information in your connection string, you should move it out of source code. See http://go.microsoft.com/fwlink/?LinkId=723263 for guidance on storing connection strings.
                optionsBuilder.UseSqlServer("Server=54.37.136.172;Database=WhereToGo;user id=sa;password=PEZET@2019;Persist Security Info=True;MultipleActiveResultSets=True");
            }
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Authentications>(entity =>
            {
                entity.HasKey(e => e.IdAuthentication);

                entity.Property(e => e.IdAuthentication).HasColumnName("idAuthentication");

                entity.Property(e => e.IsBlocked).HasColumnName("isBlocked");

                entity.Property(e => e.MacId)
                    .HasColumnName("MAC_ID")
                    .HasMaxLength(255)
                    .IsUnicode(false);

                entity.Property(e => e.NameDevice)
                    .HasColumnName("nameDevice")
                    .HasMaxLength(255)
                    .IsUnicode(false);
            });

            modelBuilder.Entity<BuildingImages>(entity =>
            {
                entity.HasKey(e => e.IdImage);

                entity.Property(e => e.IdImage).HasColumnName("idImage");

                entity.Property(e => e.BuildingLevel).HasColumnName("buildingLevel");

                entity.Property(e => e.IdBuilding).HasColumnName("idBuilding");

                entity.Property(e => e.NorthPointAngle).HasColumnName("northPointAngle");

                entity.Property(e => e.PathImage)
                    .HasColumnName("pathImage")
                    .HasColumnType("image");

                entity.HasOne(d => d.IdBuildingNavigation)
                    .WithMany(p => p.BuildingImages)
                    .HasForeignKey(d => d.IdBuilding)
                    .HasConstraintName("FK_BuildingImages_Buildings").OnDelete(DeleteBehavior.Cascade);
            });

            modelBuilder.Entity<Buildings>(entity =>
            {
                entity.HasKey(e => e.IdBuilding);

                entity.Property(e => e.IdBuilding).HasColumnName("idBuilding");

                entity.Property(e => e.IdUser).HasColumnName("idUser");

                entity.Property(e => e.ImageBuilding)
                    .HasColumnName("imageBuilding")
                    .HasColumnType("image");

                entity.Property(e => e.NameBuilding)
                    .HasColumnName("nameBuilding")
                    .HasMaxLength(255)
                    .IsUnicode(false);

                entity.Property(e => e.Scale).HasColumnName("scale");

                entity.HasOne(d => d.IdUserNavigation)
                    .WithMany(p => p.Buildings)
                    .HasForeignKey(d => d.IdUser)
                    .HasConstraintName("FK_Buildings_Users").OnDelete(DeleteBehavior.Cascade);
            });

            modelBuilder.Entity<Groups>(entity =>
            {
                entity.HasKey(e => e.IdGroup);

                entity.Property(e => e.IdGroup).HasColumnName("idGroup");

                entity.Property(e => e.IdBuilding).HasColumnName("idBuilding");

                entity.Property(e => e.ImageGroup)
                    .HasColumnName("imageGroup")
                    .HasColumnType("image");

                entity.Property(e => e.NameGroup)
                    .HasColumnName("nameGroup")
                    .HasMaxLength(255)
                    .IsUnicode(false);

                entity.HasOne(d => d.IdBuildingNavigation)
                    .WithMany(p => p.Groups)
                    .HasForeignKey(d => d.IdBuilding)
                    .HasConstraintName("FK_Groups_Buildings").OnDelete(DeleteBehavior.Cascade);
            });

            modelBuilder.Entity<OutdoorGame>(entity =>
            {
                entity.HasKey(e => e.IdOutdoorGame);

                entity.Property(e => e.IdOutdoorGame).HasColumnName("idOutdoorGame");

                entity.Property(e => e.EndDateGame).HasColumnType("datetime");

                entity.Property(e => e.IdBuilding).HasColumnName("idBuilding");

                entity.Property(e => e.IdFirstPoint).HasColumnName("idFirstPoint");

                entity.Property(e => e.ImageGame)
                    .HasColumnName("imageGame")
                    .HasColumnType("image");

                entity.Property(e => e.NameGame)
                    .HasColumnName("nameGame")
                    .HasMaxLength(255)
                    .IsUnicode(false);

                entity.Property(e => e.StartDateGame).HasColumnType("datetime");

                entity.HasOne(d => d.IdBuildingNavigation)
                    .WithMany(p => p.OutdoorGame)
                    .HasForeignKey(d => d.IdBuilding)
                    .HasConstraintName("FK_OutdoorGame_Buildings").OnDelete(DeleteBehavior.Cascade);

                entity.HasOne(d => d.IdFirstPointNavigation)
                    .WithMany(p => p.OutdoorGame)
                    .HasForeignKey(d => d.IdFirstPoint)
                    .HasConstraintName("FK_OutdoorGame_OutdoorGamePath").OnDelete(DeleteBehavior.Cascade);
            });

            modelBuilder.Entity<OutdoorGameHints>(entity =>
            {
                entity.HasKey(e => e.IdHints);

                entity.Property(e => e.IdHints).HasColumnName("idHints");

                entity.Property(e => e.Hint)
                    .HasMaxLength(255)
                    .IsUnicode(false);

                entity.Property(e => e.IdOutdoorGame).HasColumnName("idOutdoorGame");

                entity.Property(e => e.IdPoint).HasColumnName("idPoint");

                entity.HasOne(d => d.IdOutdoorGameNavigation)
                    .WithMany(p => p.OutdoorGameHints)
                    .HasForeignKey(d => d.IdOutdoorGame)
                    .HasConstraintName("FK_OutdoorGameHints_OutdoorGame").OnDelete(DeleteBehavior.Cascade);

                entity.HasOne(d => d.IdPointNavigation)
                    .WithMany(p => p.OutdoorGameHints)
                    .HasForeignKey(d => d.IdPoint)
                    .HasConstraintName("FK_OutdoorGameHints_Points").OnDelete(DeleteBehavior.Cascade);
            });

            modelBuilder.Entity<OutdoorGamePath>(entity =>
            {
                entity.HasKey(e => e.IdQuestionPoint);

                entity.Property(e => e.IdQuestionPoint).HasColumnName("idQuestionPoint");

                entity.Property(e => e.Answer)
                    .HasMaxLength(255)
                    .IsUnicode(false);

                entity.Property(e => e.IdHintPoint).HasColumnName("idHintPoint");

                entity.Property(e => e.IdNextPoint).HasColumnName("idNextPoint");

                entity.Property(e => e.IdOutdoorGame).HasColumnName("idOutdoorGame");

                entity.Property(e => e.IdPoint).HasColumnName("idPoint");

                entity.Property(e => e.Question)
                    .HasMaxLength(255)
                    .IsUnicode(false);

                entity.HasOne(d => d.IdHintPointNavigation)
                    .WithMany(p => p.OutdoorGamePath)
                    .HasForeignKey(d => d.IdHintPoint)
                    .HasConstraintName("FK_OutdoorGamePath_Points1").OnDelete(DeleteBehavior.Cascade);

                entity.HasOne(d => d.IdNextPointNavigation)
                    .WithMany(p => p.InverseIdNextPointNavigation)
                    .HasForeignKey(d => d.IdNextPoint)
                    .HasConstraintName("FK_OutdoorGamePath_Points2").OnDelete(DeleteBehavior.Cascade);

                entity.HasOne(d => d.IdOutdoorGameNavigation)
                    .WithMany(p => p.OutdoorGamePath)
                    .HasForeignKey(d => d.IdOutdoorGame)
                    .HasConstraintName("FK_OutdoorGamePath_OutdoorGame").OnDelete(DeleteBehavior.Cascade);

                entity.HasOne(d => d.IdPointNavigation)
                    .WithMany(p => p.OutdoorGamePath)
                    .HasForeignKey(d => d.IdPoint)
                    .HasConstraintName("FK_OutdoorGamePath_Points").OnDelete(DeleteBehavior.Cascade);
            });

            modelBuilder.Entity<OutdoorGameRecordTime>(entity =>
            {
                entity.HasKey(e => e.IdRecord)
                    .HasName("PK_OutdoorGameRecordtime");

                entity.Property(e => e.IdRecord).HasColumnName("idRecord");

                entity.Property(e => e.EndDate).HasColumnType("datetime");

                entity.Property(e => e.IdOutdoorGame).HasColumnName("idOutdoorGame");

                entity.Property(e => e.Mac)
                    .HasColumnName("MAC")
                    .HasMaxLength(255)
                    .IsUnicode(false);

                entity.Property(e => e.StartDate).HasColumnType("datetime");

                entity.Property(e => e.Name)
                    .HasColumnName("Name")
                    .HasMaxLength(255)
                    .IsUnicode(false);

                entity.HasOne(d => d.IdOutdoorGameNavigation)
                    .WithMany(p => p.OutdoorGameRecordTime)
                    .HasForeignKey(d => d.IdOutdoorGame)
                    .HasConstraintName("FK_OutdoorGameRecordTime_OutdoorGame").OnDelete(DeleteBehavior.Cascade);
            });

            modelBuilder.Entity<PointType>(entity =>
            {
                entity.HasKey(e => e.IdPointType);

                entity.Property(e => e.IdPointType)
                    .HasColumnName("idPointType")
                    .ValueGeneratedNever();

                entity.Property(e => e.TypePoint)
                    .HasColumnName("typePoint")
                    .HasMaxLength(255)
                    .IsUnicode(false);
            });

            modelBuilder.Entity<Points>(entity =>
            {
                entity.HasKey(e => e.IdPoint);

                entity.Property(e => e.IdPoint).HasColumnName("idPoint");

                entity.Property(e => e.IdImage).HasColumnName("idImage");

                entity.Property(e => e.IdPointType).HasColumnName("idPointType");

                entity.Property(e => e.ImagePoint)
                    .HasColumnName("imagePoint")
                    .HasColumnType("image");

                entity.Property(e => e.X).HasColumnName("x");

                entity.Property(e => e.Y).HasColumnName("y");

                entity.HasOne(d => d.IdImageNavigation)
                    .WithMany(p => p.Points)
                    .HasForeignKey(d => d.IdImage)
                    .HasConstraintName("FK_Points_BuildingImages").OnDelete(DeleteBehavior.Cascade);
            });

            modelBuilder.Entity<PointsConnection>(entity =>
            {
                entity.HasKey(e => e.IdPointConnection);

                entity.Property(e => e.IdPointConnection).HasColumnName("idPointConnection");

                entity.Property(e => e.IdPointEnd).HasColumnName("idPointEnd");

                entity.Property(e => e.IdPointStart).HasColumnName("idPointStart");

                entity.HasOne(d => d.IdPointEndNavigation)
                    .WithMany(p => p.PointsConnectionIdPointEndNavigation)
                    .HasForeignKey(d => d.IdPointEnd)
                    .HasConstraintName("FK_PointsConnection_Points1").OnDelete(DeleteBehavior.Cascade);

                entity.HasOne(d => d.IdPointStartNavigation)
                    .WithMany(p => p.PointsConnectionIdPointStartNavigation)
                    .HasForeignKey(d => d.IdPointStart)
                    .HasConstraintName("FK_PointsConnection_Points").OnDelete(DeleteBehavior.Cascade);
            });

            modelBuilder.Entity<PointsDetail>(entity =>
            {
                entity.HasKey(e => e.IdPointDetails);

                entity.Property(e => e.IdPointDetails).HasColumnName("idPointDetails");

                entity.Property(e => e.IdGroup).HasColumnName("idGroup");

                entity.Property(e => e.IdPoint).HasColumnName("idPoint");

                entity.Property(e => e.ImagePoint).HasColumnType("image");

                entity.Property(e => e.NamePoint)
                    .HasColumnName("namePoint")
                    .HasMaxLength(255)
                    .IsUnicode(false);

                entity.HasOne(d => d.IdGroupNavigation)
                    .WithMany(p => p.PointsDetail)
                    .HasForeignKey(d => d.IdGroup)
                    .HasConstraintName("FK_PointsDetail_Groups").OnDelete(DeleteBehavior.Cascade);

                entity.HasOne(d => d.IdPointNavigation)
                    .WithMany(p => p.PointsDetail)
                    .HasForeignKey(d => d.IdPoint)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_PointsDetail_Points").OnDelete(DeleteBehavior.Cascade);
            });

            modelBuilder.Entity<Users>(entity =>
            {
                entity.HasKey(e => e.IdUser);

                entity.Property(e => e.IdUser).HasColumnName("idUser");

                entity.Property(e => e.Email)
                    .HasColumnName("email")
                    .HasMaxLength(255)
                    .IsUnicode(false);

                entity.Property(e => e.Name)
                    .HasColumnName("name")
                    .HasMaxLength(255)
                    .IsUnicode(false);

                entity.Property(e => e.Password)
                    .HasColumnName("password")
                    .HasMaxLength(255)
                    .IsUnicode(false);
            });

            OnModelCreatingPartial(modelBuilder);
        }

        partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
    }
}
