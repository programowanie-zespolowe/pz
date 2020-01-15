﻿using System;
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
        public virtual DbSet<PointType> PointType { get; set; }
        public virtual DbSet<Points> Points { get; set; }
        public virtual DbSet<PointsConnections> PointsConnections { get; set; }
        public virtual DbSet<Users> Users { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (!optionsBuilder.IsConfigured)
            {
#warning To protect potentially sensitive information in your connection string, you should move it out of source code. See http://go.microsoft.com/fwlink/?LinkId=723263 for guidance on storing connection strings.
                optionsBuilder.UseSqlServer("Server=54.37.136.172;Database=WhereToGo;user id=sa;password=PEZET@2019;Persist Security Info=True;");
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

                entity.Property(e => e.Scale).HasColumnName("scale");

                entity.HasOne(d => d.IdBuildingNavigation)
                    .WithMany(p => p.BuildingImages)
                    .HasForeignKey(d => d.IdBuilding)
                    .HasConstraintName("FK_BuildingImages_Buildings");
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

                entity.HasOne(d => d.IdUserNavigation)
                    .WithMany(p => p.Buildings)
                    .HasForeignKey(d => d.IdUser)
                    .HasConstraintName("FK_Buildings_Users");
            });

            modelBuilder.Entity<Groups>(entity =>
            {
                entity.HasKey(e => e.IdGroup);

                entity.Property(e => e.IdGroup).HasColumnName("idGroup");

                entity.Property(e => e.ImageGroup)
                    .HasColumnName("imageGroup")
                    .HasColumnType("image");

                entity.Property(e => e.NameGroup)
                    .HasColumnName("nameGroup")
                    .HasMaxLength(255)
                    .IsUnicode(false);
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

                entity.Property(e => e.IdGroup).HasColumnName("idGroup");

                entity.Property(e => e.IdImage).HasColumnName("idImage");

                entity.Property(e => e.IdPointType).HasColumnName("idPointType");

                entity.Property(e => e.ImagePoint)
                    .HasColumnName("imagePoint")
                    .HasColumnType("image");

                entity.Property(e => e.NamePoint)
                    .HasColumnName("namePoint")
                    .HasMaxLength(255)
                    .IsUnicode(false);

                entity.Property(e => e.X).HasColumnName("x");

                entity.Property(e => e.Y).HasColumnName("y");

                entity.HasOne(d => d.IdGroupNavigation)
                    .WithMany(p => p.Points)
                    .HasForeignKey(d => d.IdGroup)
                    .HasConstraintName("FK_Points_Groups");

                entity.HasOne(d => d.IdImageNavigation)
                    .WithMany(p => p.Points)
                    .HasForeignKey(d => d.IdImage)
                    .HasConstraintName("FK_Points_BuildingImages");

                entity.HasOne(d => d.IdPointTypeNavigation)
                    .WithMany(p => p.Points)
                    .HasForeignKey(d => d.IdPointType)
                    .HasConstraintName("FK_Points_PointType");
            });

            modelBuilder.Entity<PointsConnections>(entity =>
            {
                entity.HasKey(e => e.IdPointConnection);

                entity.Property(e => e.IdPointConnection).HasColumnName("idPointConnection");

                entity.Property(e => e.Distance).HasColumnName("distance");

                entity.Property(e => e.IdPointEnd).HasColumnName("idPointEnd");

                entity.Property(e => e.IdPointStart).HasColumnName("idPointStart");

                entity.HasOne(d => d.IdPointEndNavigation)
                    .WithMany(p => p.PointsConnectionsIdPointEndNavigation)
                    .HasForeignKey(d => d.IdPointEnd)
                    .HasConstraintName("FK_PointsConnections_Points1");

                entity.HasOne(d => d.IdPointStartNavigation)
                    .WithMany(p => p.PointsConnectionsIdPointStartNavigation)
                    .HasForeignKey(d => d.IdPointStart)
                    .HasConstraintName("FK_PointsConnections_Points");
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
