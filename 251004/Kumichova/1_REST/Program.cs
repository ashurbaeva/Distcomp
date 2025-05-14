using LAB1.Domain;
using LAB1.Interfaces;
using LAB1.Services;

var builder = WebApplication.CreateBuilder(args);

// Регистрация сервисов
builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

// Регистрация репозиториев
builder.Services.AddSingleton<IRepository<User>, InMemoryRepository<User>>();
builder.Services.AddSingleton<IRepository<Topic>, InMemoryRepository<Topic>>();
builder.Services.AddSingleton<IRepository<Tag>, InMemoryRepository<Tag>>();
builder.Services.AddSingleton<IRepository<Message>, InMemoryRepository<Message>>();

// Регистрация сервисов
builder.Services.AddSingleton<UserService>();
builder.Services.AddSingleton<TopicService>();
builder.Services.AddSingleton<TagService>();
builder.Services.AddSingleton<MessageService>();

var app = builder.Build();

if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();
app.MapControllers();
app.Run("http://localhost:24110");