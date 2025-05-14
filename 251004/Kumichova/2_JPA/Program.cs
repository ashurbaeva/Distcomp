using LAB2.Data;
using LAB2.Interfaces;
using LAB2.Services;
using Microsoft.EntityFrameworkCore;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container
builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

// Configure database context
builder.Services.AddDbContext<AppDbContext>(options =>
    options.UseNpgsql(builder.Configuration.GetConnectionString("Default")));

// Register repositories
builder.Services.AddScoped(typeof(IRepository<>), typeof(EfRepository<>));

// Register services
builder.Services.AddScoped<UserService>();
builder.Services.AddScoped<TopicService>();
builder.Services.AddScoped<TagService>();
builder.Services.AddScoped<MessageService>();

var app = builder.Build();

// Apply migrations
using (var scope = app.Services.CreateScope())
{
    var db = scope.ServiceProvider.GetRequiredService<AppDbContext>();
    db.Database.Migrate();
}

// Configure the HTTP request pipeline
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();
app.MapControllers();
app.Run("http://localhost:24110");