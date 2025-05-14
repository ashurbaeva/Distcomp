using Cassandra;
using CassandraMessages.Repositories;
using CassandraMessages.Services;

var builder = WebApplication.CreateBuilder(args);

// Регистрация Cassandra
builder.Services.AddSingleton<ICluster>(_ => 
    Cluster.Builder()
        .AddContactPoint("localhost")
        .WithPort(9042)
        .Build());

// Регистрация репозитория и сервиса
builder.Services.AddScoped<IMessageRepository, MessageRepository>();
builder.Services.AddScoped<MessageService>();

builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var app = builder.Build();

app.UseSwagger();
app.UseSwaggerUI();
app.UseHttpsRedirection();
app.MapControllers();
app.Run("http://localhost:24130");