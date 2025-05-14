using AutoMapper;
using LabsRV_Articles.Models.Domain;
using LabsRV_Articles.Models.DTO;
using LabsRV_Articles.Repositories;

namespace LabsRV_Articles.Services
{
    public class StickerService : Service<Sticker, StickerRequestDto, StickerResponseDto>
    {
        protected new readonly StickerRepository _repository;

        public StickerService(StickerRepository repository, IMapper mapper)
            : base(repository, mapper)
        {
            _repository = repository;
        }

        public override void Validate(StickerRequestDto request)
        {
            if (string.IsNullOrWhiteSpace(request.Name) || request.Name.Length < 2 || request.Name.Length > 32)
                throw new ArgumentException("Sticker Name must be between 2 and 32 characters.");
        }
    }
}
