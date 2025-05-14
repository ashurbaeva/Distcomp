using AutoMapper;
using Shared.Models.DTOs.Requests;
using Shared.Models.DTOs.Responses;
using Discussion.Models.Entities;

namespace Discussion.Mapping
{
    public class DiscussionMappingProfile : Profile
    {
        public DiscussionMappingProfile()
        {
            CreateMap<ReactionRequestTo, Reaction>();
            CreateMap<Reaction, ReactionResponseTo>();
        }
    }
}
