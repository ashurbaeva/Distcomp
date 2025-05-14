using LabsRV_Articles.Data;
using LabsRV_Articles.Mapping;
using LabsRV_Articles.Models.Domain;
using LabsRV_Articles.Models.Exceptions;
using LabsRV_Articles.Repositories;
using LabsRV_Articles.Services;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Text.Json;

namespace LabsRV_Articles
{
    public class Program
    {
        public static void Main(string[] args)
        {
            var builder = WebApplication.CreateBuilder(args);

            // ��������� ����������� � PostgreSQL ����� ������ �� appsettings.json
            builder.Services.AddDbContext<ApplicationDbContext>(options =>
                options.UseNpgsql(builder.Configuration.GetConnectionString("DefaultConnection")));

            // Add services to the container.

            // ������������ �����������
            builder.Services.AddControllers();
            /*.ConfigureApiBehaviorOptions(options =>
            {
                // ��������� �������� ����������� ���������� ������ ������, ����� �� ��������� JSON
                options.InvalidModelStateResponseFactory = context =>
                {
                    var result = new ObjectResult(context.ModelState)
                    {
                        StatusCode = StatusCodes.Status405MethodNotAllowed,
                        ContentTypes = { "application/json" }
                    };


                    var result = new BadRequestObjectResult(context.ModelState);
                    result.ContentTypes.Add("application/json");
                    return result;
                };
            });*/

            // ������������ AutoMapper � ����� ��������
            builder.Services.AddAutoMapper(typeof(MappingProfile));

            // ������������ InMemory-����������� ��� ������ ��������
            builder.Services.AddScoped<AuthorRepository, AuthorRepository>();
            builder.Services.AddScoped<ArticleRepository, ArticleRepository>();
            builder.Services.AddScoped<StickerRepository, StickerRepository>();

            // ������������ �������
            builder.Services.AddScoped<AuthorService>();
            builder.Services.AddScoped<ArticleService>();
            builder.Services.AddScoped<StickerService>();

            builder.Services.AddHttpClient<RemoteCommentService, RemoteCommentService>()
                .ConfigureHttpClient(client =>
                {
                    client.BaseAddress = new Uri("http://localhost:24130/api/v1.0/comments/");
                });

            var app = builder.Build();

            // ���� ������ �� ������������� �� ������ �������� ��� ���������� ��� ������,
            // ���������� UseStatusCodePages ��� ��������� � ������������ JSON-������ ������ HTML.
            app.UseStatusCodePages(async context =>
            {
                // �������� ����� � ��� ������
                var response = context.HttpContext.Response;
                response.ContentType = "application/json";

                // ����� ����� ��������� ��������� � ����������� �� ����.
                // ��������, ��� 404 � 400 � ���������� ��������������� ����������.
                string message = response.StatusCode switch
                {
                    404 => "Resource not found.",
                    400 => "Bad request.",
                    _ => "An error occurred."
                };

                var result = JsonSerializer.Serialize(new
                {
                    errorMessage = message,
                    errorCode = $"{response.StatusCode}00"
                });

                await response.WriteAsync(result);
            });

            // ���������� ��������� ������ ����� middleware
            app.UseExceptionHandler(config =>
            {
                config.Run(async context =>
                {
                    var errorFeature = context.Features.Get<Microsoft.AspNetCore.Diagnostics.IExceptionHandlerFeature>();
                    if (errorFeature != null)
                    {
                        var ex = errorFeature.Error;

                        if (ex is ArgumentException)
                            context.Response.StatusCode = 400; // Bad Request
                        else if (ex is KeyNotFoundException)
                            context.Response.StatusCode = 404; // Not Found
                        else if (ex is AlreadyExistsException)
                            context.Response.StatusCode = 403;
                        else
                            context.Response.StatusCode = 400; // Bad Request

                        context.Response.ContentType = "application/json";
                        var errResponse = new
                        {
                            errorMessage = ex.Message,
                            errorCode = $"{context.Response.StatusCode}00"
                        };
                        await context.Response.WriteAsJsonAsync(errResponse);
                    }
                });
            });


            // Configure the HTTP request pipeline.

            app.UseHttpsRedirection();

            app.UseAuthorization();


            app.MapControllers();

            // ����������� ���������� �� ������������� ����� 24110
            app.Urls.Clear();
            app.Urls.Add("http://localhost:24110");

            app.Run();
        }
    }
}
