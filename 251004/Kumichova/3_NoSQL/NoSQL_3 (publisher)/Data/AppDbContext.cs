using LAB2.Domain;
using Microsoft.EntityFrameworkCore;

namespace LAB2.Data;

public class AppDbContext : DbContext
{
    public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        // User configuration
        modelBuilder.Entity<User>(e =>
        {
            e.ToTable("tbl_user");
            e.HasKey(u => u.id);
            e.Property(u => u.Login).HasMaxLength(64).IsRequired();
            e.Property(u => u.Password).HasMaxLength(128).IsRequired();
            e.Property(u => u.Firstname).HasMaxLength(64).IsRequired();
            e.Property(u => u.Lastname).HasMaxLength(64).IsRequired();
            e.HasIndex(u => u.Login).IsUnique();

            e.HasMany(u => u.Topics)
                .WithOne(t => t.User)
                .HasForeignKey(t => t.UserId)
                .OnDelete(DeleteBehavior.Cascade);
        });

        // Topic configuration
        modelBuilder.Entity<Topic>(e =>
        {
            e.ToTable("tbl_topic");
            e.HasKey(t => t.id);
            e.Property(t => t.Title).HasMaxLength(64).IsRequired();
            e.Property(t => t.Content).HasMaxLength(2048).IsRequired();

            e.HasOne(t => t.User)
                .WithMany(u => u.Topics)
                .HasForeignKey(t => t.UserId)
                .OnDelete(DeleteBehavior.Cascade);

            e.HasMany(t => t.Messages)
                .WithOne(m => m.Topic)
                .HasForeignKey(m => m.TopicId)
                .OnDelete(DeleteBehavior.Cascade);

            e.HasMany(t => t.Tags)
                .WithMany(tg => tg.Topics)
                .UsingEntity<TopicTag>(
                    j => j.HasOne(tt => tt.Tag)
                        .WithMany()
                        .HasForeignKey(tt => tt.TagId),
                    j => j.HasOne(tt => tt.Topic)
                        .WithMany()
                        .HasForeignKey(tt => tt.TopicId),
                    j => j.ToTable("tbl_topic_tag")
                );
        });

        // Tag configuration
        modelBuilder.Entity<Tag>(e =>
        {
            e.ToTable("tbl_tag");
            e.HasKey(t => t.id);
            e.Property(t => t.Name).HasMaxLength(32).IsRequired();
        });

        // Message configuration
        modelBuilder.Entity<Message>(e =>
        {
            e.ToTable("tbl_message");
            e.HasKey(m => m.id);
            e.Property(m => m.Content).HasMaxLength(2048).IsRequired();
        });
    }

    public DbSet<User> Users { get; set; }
    public DbSet<Topic> Topics { get; set; }
    public DbSet<Tag> Tags { get; set; }
    public DbSet<Message> Messages { get; set; }
    public DbSet<TopicTag> TopicTags { get; set; }
}