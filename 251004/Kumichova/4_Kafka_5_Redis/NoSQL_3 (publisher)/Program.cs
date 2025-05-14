using LAB2.Data;
using LAB2.Interfaces;
using LAB2.Services;
using LAB2.Kafka;
using Microsoft.EntityFrameworkCore;

var builder = WebApplication.CreateBuilder(args);

// Добавление контроллеров, Swagger
builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

// Конфигурация базы данных PostgreSQL
builder.Services.AddDbContext<AppDbContext>(options =>
    options.UseNpgsql(builder.Configuration.GetConnectionString("Default")));

// Регистрация репозиториев и сервисов
builder.Services.AddScoped(typeof(IRepository<>), typeof(EfRepository<>));
builder.Services.AddScoped<UserService>();
builder.Services.AddScoped<TopicService>();
builder.Services.AddScoped<TagService>();
builder.Services.AddScoped<MessageService>();

// Конфигурация Kafka
builder.Services.Configure<KafkaSettings>(builder.Configuration.GetSection("Kafka"));
builder.Services.AddSingleton<KafkaProducerService>();
builder.Services.AddHostedService<KafkaConsumerService>();

var app = builder.Build();

// Применение миграций при запуске
using (var scope = app.Services.CreateScope())
{
    var db = scope.ServiceProvider.GetRequiredService<AppDbContext>();
    db.Database.Migrate();
    
    // Инициализация Kafka producer
    var kafkaProducer = scope.ServiceProvider.GetRequiredService<KafkaProducerService>();
    try
    {
        // Проверка подключения к Kafka
        await kafkaProducer.ProduceAsync(new { Test = "Connection test" });
        Console.WriteLine("Kafka connection successful");
    }
    catch (Exception ex)
    {
        Console.WriteLine($"Kafka connection failed: {ex.Message}");
        // Здесь можно добавить логику повторных попыток или завершения приложения
    }
}

// Конфигурация Middleware
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();
app.UseAuthorization();
app.MapControllers();

try
{
    await app.RunAsync("http://localhost:24110");
}
catch (Exception ex)
{
    Console.WriteLine($"Failed to start server: {ex.Message}");
    // Попробовать альтернативный порт
    await app.RunAsync("http://localhost:24111");
}