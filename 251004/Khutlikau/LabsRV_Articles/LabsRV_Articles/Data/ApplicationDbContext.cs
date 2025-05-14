using LabsRV_Articles.Models.Domain;
using LabsRV_Articles.MyApp.Models.Domain;
using Microsoft.EntityFrameworkCore;

namespace LabsRV_Articles.Data
{
    public class ApplicationDbContext : DbContext
    {
        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options)
            : base(options)
        { }

        public DbSet<Author> Authors { get; set; }
        public DbSet<Article> Articles { get; set; }
        //public DbSet<Comment> Comments { get; set; }
        public DbSet<Sticker> Stickers { get; set; }
        public DbSet<ArticleSticker> ArticleStickers { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            // Задаём схему по умолчанию (например, "distcomp")
            // modelBuilder.HasDefaultSchema("distcomp");

            // Указываем, что таблицы обязательно должны иметь префикс "tbl_"
            modelBuilder.Entity<Author>().ToTable("tbl_author");
            modelBuilder.Entity<Article>().ToTable("tbl_article");
            //modelBuilder.Entity<Comment>().ToTable("tbl_comment");
            modelBuilder.Entity<Sticker>().ToTable("tbl_sticker");
            modelBuilder.Entity<ArticleSticker>().ToTable("tbl_article_sticker");

            // Настройка связи "один-ко-многим": Author → Article
            modelBuilder.Entity<Author>()
                .HasMany(a => a.articles)
                .WithOne(a => a.author)
                .HasForeignKey(a => a.authorId)
                .OnDelete(DeleteBehavior.Cascade);

            // Настройка связи "один-ко-многим": Article → Comment
            /*modelBuilder.Entity<Article>()
                .HasMany(a => a.comments)
                .WithOne(c => c.article)
                .HasForeignKey(c => c.articleId)
                .OnDelete(DeleteBehavior.Cascade);*/

            // Настройка связи "многие-ко-многим" через вспомогательную сущность ArticleSticker:
            // Вспомогательная таблица ArticleSticker содержит составной ключ (ArticleId, StickerId)
            modelBuilder.Entity<ArticleSticker>()
                .HasKey(as_ => new { as_.articleId, as_.stickerId });

            modelBuilder.Entity<ArticleSticker>()
                .HasOne(as_ => as_.article)
                .WithMany(a => a.articleStickers)
                .HasForeignKey(as_ => as_.articleId)    
                .OnDelete(DeleteBehavior.Cascade); // Удаление связей m2m при удалении статьи

            modelBuilder.Entity<ArticleSticker>()
                .HasOne(as_ => as_.sticker)
                .WithMany(s => s.articleStickers)
                .HasForeignKey(as_ => as_.stickerId);
        }
    }
}

