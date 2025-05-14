using DistComp.Models;
using Microsoft.EntityFrameworkCore;

namespace DistComp.Data;

public class AppDbContext : DbContext
{
    public DbSet<Creator> Creators { get; set; }
    public DbSet<Issue> Issues { get; set; }
    public DbSet<Note> Notes { get; set; }
    public DbSet<Tag> Tags { get; set; }
    
    public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) 
    { }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        base.OnModelCreating(modelBuilder);

        modelBuilder.Entity<Creator>()
            .HasIndex(u => u.Login)
            .IsUnique();
        
     
        modelBuilder.Entity<Creator>()
            .ToTable("tbl_creator");
        
        modelBuilder.Entity<Tag>()
            .ToTable("tbl_tags");
        
        modelBuilder.Entity<Issue>()
            .ToTable("tbl_issue");
        
        modelBuilder.Entity<Note>()
            .ToTable("tbl_note");
        
        modelBuilder.Entity<Issue>()
            .HasIndex(s => s.Title)
            .IsUnique();

        modelBuilder.Entity<Tag>()
            .HasIndex(t => t.Name)
            .IsUnique();
    }
}