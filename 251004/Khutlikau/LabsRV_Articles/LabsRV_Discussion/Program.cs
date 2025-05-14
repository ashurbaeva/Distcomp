using AutoMapper;
using LabsRV_Discussion.Mapping;
using LabsRV_Discussion.Models.Domain;
using LabsRV_Discussion.Repositories;
using LabsRV_Discussion.Services;
using MongoDB.Driver;

namespace LabsRV_Discussion
{
    public class Program
    {
        public static void Main(string[] args)
        {
            var builder = WebApplication.CreateBuilder(args);

            // Add services to the container

            // Настройка MongoDB
            var client = new MongoClient(builder.Configuration["MongoDb:ConnectionString"]);
            var database = client.GetDatabase(builder.Configuration["MongoDb:DatabaseName"]);
            var collection = database.GetCollection<Comment>(builder.Configuration["MongoDb:CommentCollectionName"]);

            builder.Services.AddControllers();

            builder.Services.AddSingleton<IMongoClient>(client);
            builder.Services.AddSingleton(collection);
            builder.Services.AddSingleton<MongoCommentRepository>();

            // Настройка маппера
            builder.Services.AddAutoMapper(typeof(MappingProfile));
            builder.Services.AddSingleton<CommentService>();

            // Настройка порта
            builder.WebHost.UseUrls("http://localhost:24130");

            var app = builder.Build();


            // Configure the HTTP request pipeline.

            app.UseHttpsRedirection();

            //app.UseAuthorization();


            app.MapControllers();

            app.Run();
        }
    }
}
