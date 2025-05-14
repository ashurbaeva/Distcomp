﻿using AutoMapper;
using Discussion.DTO.RequestDTO;
using Discussion.DTO.ResponseDTO;
using Discussion.Models;

namespace Discussion.Infrastructure.Mapper;

public class MappingProfile : Profile
{
    public MappingProfile()
    {
        CreateMap<Note, LabelResponseDTO>();
        CreateMap<NoteRequestDTO, Note>();
    }
}