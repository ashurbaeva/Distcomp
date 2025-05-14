using Microsoft.EntityFrameworkCore;
using Microsoft.OpenApi.Models;
using Npgsql;
using Publisher.Data;
using Publisher.Models.Entities;
using Publisher.Services;
using Publisher.Storage;
using Publisher.Interfaces;
using Publisher.Mapping;
using Confluent.Kafka;
using Publisher.Kafka;
using System.Net.Http;

var builder = WebApplication.CreateBuilder(args);

var connectionString = builder.Configuration.GetConnectionString("DefaultConnection");
builder.Services.AddDbContext<AppDbContext>(options =>
    options.UseNpgsql(connectionString));

builder.Services.AddSwaggerGen(c =>
{
    c.SwaggerDoc("v1", new OpenApiInfo
    {
        Title = "My API",
        Version = "v1",
        Description = "Beautiful REST API",
        Contact = new OpenApiContact { Name = "My Name" },
    });

});

builder.Services.AddControllers();

builder.Services.AddProblemDetails();
builder.Services.AddEndpointsApiExplorer();
// Learn more about configuring OpenAPI at https://aka.ms/aspnet/openapi

builder.Services.AddAutoMapper(typeof(PublisherMappingProfiles));

builder.Services.AddScoped<ICrudRepository<Editor>, EditorRepository>();
builder.Services.AddScoped<ICrudRepository<News>, NewsRepository>();
builder.Services.AddScoped<ICrudRepository<Sticker>, StickerRepository>();
builder.Services.AddScoped<NewsStickerRepository>();

builder.Services.AddScoped<EditorRepository>();
builder.Services.AddScoped<NewsRepository>();
builder.Services.AddScoped<StickerRepository>();

builder.Services.AddScoped<EditorService>();
builder.Services.AddScoped<StickerService>();

builder.Services.AddScoped<NewsService>();

builder.Services.AddHttpClient("DiscussionAPI", client =>
{
    client.BaseAddress = new Uri("http://localhost:24130");
    client.DefaultRequestHeaders.Add("X-Internal-Request", "true");
});

// Kafka Producer and Consumer setup
builder.Services.AddSingleton<IProducer<string, string>>(sp =>
{
    var config = new ProducerConfig
    {
        BootstrapServers = "localhost:9092", // Adjust your Kafka server address here
    };
    return new ProducerBuilder<string, string>(config).Build();
});

builder.Services.AddSingleton<IConsumer<string, string>>(sp =>
{
    var config = new ConsumerConfig
    {
        BootstrapServers = "localhost:9092", // Adjust your Kafka server address here
        GroupId = "publisher-service",
        AutoOffsetReset = AutoOffsetReset.Earliest
    };
    return new ConsumerBuilder<string, string>(config).Build();
});

builder.Services.AddScoped<PublisherProducer>();
builder.Services.AddSingleton<PublisherConsumer>();

var app = builder.Build();

if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
    app.UseDeveloperExceptionPage();
}

app.UseRouting();
app.UseAuthorization();
app.MapControllers();

using (var scope = app.Services.CreateScope())
{
    var db = scope.ServiceProvider.GetRequiredService<AppDbContext>();
    try
    {
        if (db.Database.GetPendingMigrations().Any())
        {
            db.Database.Migrate();
        }
    }
    catch (Exception ex)
    {
        Console.WriteLine($"Error applying migrations: {ex.Message}");
        throw;
    }
}

app.Run();