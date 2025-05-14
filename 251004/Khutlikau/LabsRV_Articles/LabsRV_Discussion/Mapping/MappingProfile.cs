using AutoMapper;
using LabsRV_Discussion.Models.Domain;
using LabsRV_Discussion.Models.DTO;
using MongoDB.Bson;
using static System.Runtime.InteropServices.JavaScript.JSType;

namespace LabsRV_Discussion.Mapping
{
    public class MappingProfile : Profile
    {
        public MappingProfile()
        {
            // Маппинг из DTO в MongoDB модель
            CreateMap<CommentRequestDto, Comment>();

            // Маппинг из MongoDB модели в DTO
            CreateMap<Comment, CommentResponseDto>();
        }
    }
}
