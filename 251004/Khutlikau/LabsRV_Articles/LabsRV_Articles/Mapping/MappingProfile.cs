using AutoMapper;
using LabsRV_Articles.Models.Domain;
using LabsRV_Articles.Models.DTO;

namespace LabsRV_Articles.Mapping
{
    public class MappingProfile : Profile
    {
        public MappingProfile()
        {
            // Маппинг для авторов с обязательным заполнением списка ArticleIds
            CreateMap<Author, AuthorResponseDto>()
                .ForMember(dest => dest.ArticleIds, opt => opt.MapFrom(src => src.articles.Select(a => a.id).ToList()));
            CreateMap<AuthorRequestDto, Author>();

            CreateMap<Article, ArticleResponseDto>()
                .ForMember(dest => dest.StickerNames,
                            opt => opt.MapFrom(src => src.articleStickers.Select(as_ => as_.sticker.name)));

            // Маппинг для статей
            CreateMap<Article, ArticleResponseDto>();
            CreateMap<ArticleRequestDto, Article>();

            // Маппинг для комментариев
            //CreateMap<Comment, CommentResponseDto>();
            //CreateMap<CommentRequestDto, Comment>();

            // Маппинг для стикеров
            CreateMap<Sticker, StickerResponseDto>();
            CreateMap<StickerRequestDto, Sticker>();
        }
    }
}
