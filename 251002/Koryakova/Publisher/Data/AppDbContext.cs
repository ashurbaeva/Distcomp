using Microsoft.EntityFrameworkCore;
using Publisher.Models.Entities;

namespace Publisher.Data
{
    public class AppDbContext : DbContext
    {
        public DbSet<Editor> Editors { get; set; }
        public DbSet<News> News { get; set; }
        public DbSet<Sticker> Stickers { get; set; }
        public DbSet<NewsSticker> NewsStickers { get; set; }

        public AppDbContext(DbContextOptions<AppDbContext> options)
            : base(options) { }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<News>()
                .HasOne<Editor>()
                .WithMany()
                .HasForeignKey(n => n.EditorId)
                .OnDelete(DeleteBehavior.SetNull);

            modelBuilder.Entity<NewsSticker>(ns =>
            {
                // Composite alternate key (enforces unique pairs)
                ns.HasAlternateKey(ns => new { ns.NewsId, ns.StickerId });

                ns.HasOne<News>()
                  .WithMany()
                  .HasForeignKey(ns => ns.NewsId)
                  .OnDelete(DeleteBehavior.Cascade);

                ns.HasOne<Sticker>()
                  .WithMany()
                  .HasForeignKey(ns => ns.StickerId)
                  .OnDelete(DeleteBehavior.Cascade);
            });
                                 
            foreach (var entity in modelBuilder.Model.GetEntityTypes())
            {
                entity.SetTableName(entity.GetTableName().ToLower());
                foreach (var property in entity.GetProperties())
                {
                    property.SetColumnName(ToSnakeCase(property.Name));
                }
            }

            modelBuilder.Entity<Editor>().Property(e => e.FirstName).HasColumnName("firstname");
            modelBuilder.Entity<Editor>().Property(e => e.LastName).HasColumnName("lastname");

            modelBuilder.Entity<Editor>().ToTable("tbl_editor");
            modelBuilder.Entity<News>().ToTable("tbl_news");
            modelBuilder.Entity<Sticker>().ToTable("tbl_sticker");
            modelBuilder.Entity<NewsSticker>().ToTable("tbl_news_sticker");              
        }

        private string ToSnakeCase(string input)
            => string.Concat(input.Select((c, i) =>
            i > 0 && char.IsUpper(c) ? "_" + c.ToString() : c.ToString())).ToLower();
    }
}
