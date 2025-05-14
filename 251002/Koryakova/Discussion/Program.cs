using Discussion.Mapping;
using Discussion.Models.Entities;
using Discussion.Services;
using Discussion.Storage;
using Microsoft.Extensions.Options;
using MongoDB.Driver;
using Discussion.Interfaces;
using Discussion.Data;
using System.Reflection;
using Confluent.Kafka;
using Discussion.Kafka;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container
builder.Services.AddControllers();


builder.Services.AddControllers(options =>
{
    options.SuppressAsyncSuffixInActionNames = false;
});

// Configure MongoDB
builder.Services.Configure<MongoDbSettings>(
    builder.Configuration.GetSection("MongoDb"));
builder.Services.AddSingleton<IMongoClient>(serviceProvider =>
    new MongoClient(serviceProvider.GetRequiredService<IOptions<MongoDbSettings>>().Value.ConnectionString));
builder.Services.AddScoped<IMongoDatabase>(serviceProvider => {
    var settings = serviceProvider.GetRequiredService<IOptions<MongoDbSettings>>().Value;
    var client = serviceProvider.GetRequiredService<IMongoClient>();
    return client.GetDatabase(settings.DatabaseName);
});


builder.Services.AddSingleton<IProducer<string, string>>(sp =>
{
    var config = new ProducerConfig { BootstrapServers = "localhost:9092" };
    return new ProducerBuilder<string, string>(config).Build();
});
builder.Services.AddSingleton<ReactionProducer>();

builder.Services.AddScoped<ReactionConsumer>();


builder.Services.AddHostedService<ConsumerHostedService>();

// Register your repository and services
builder.Services.AddScoped<ICrudRepository<Reaction>, ReactionRepository>();
    builder.Services.AddScoped<ReactionService>();
   // builder.Services.AddScoped<IHostedService, ReactionConsumer>();

    builder.Services.AddAutoMapper(typeof(DiscussionMappingProfile));

    // Add Swagger for testing
    builder.Services.AddEndpointsApiExplorer();
    builder.Services.AddSwaggerGen();

    var app = builder.Build();

    // Configure the HTTP request pipeline
    if (app.Environment.IsDevelopment())
    {
        app.UseSwagger();
        app.UseSwaggerUI();
    }

    //app.UseHttpsRedirection();
    app.UseAuthorization();
    app.MapControllers();

    app.Run();
//}