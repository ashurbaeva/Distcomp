using AutoMapper;
using Publisher.Models.DTOs.Requests;
using Publisher.Models.DTOs.Responses;
using Publisher.Models.Entities;

namespace Publisher.Mapping
{
    public class PublisherMappingProfiles : Profile
    {
        public PublisherMappingProfiles()
        {
            CreateMap<EditorRequestTo, Editor>(); // Request → Entity
            CreateMap<Editor, EditorResponseTo>(); // Entity → Response

            CreateMap<NewsRequestTo, News>(); 
            CreateMap<News, NewsResponseTo>();

            CreateMap<StickerRequestTo, Sticker>();
            CreateMap<Sticker, StickerResponseTo>();
        }
    }
}
