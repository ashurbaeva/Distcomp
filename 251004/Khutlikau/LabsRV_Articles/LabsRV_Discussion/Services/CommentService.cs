using AutoMapper;
using LabsRV_Discussion.Models.Domain;
using LabsRV_Discussion.Models.DTO;
using LabsRV_Discussion.Repositories;
using System.Collections;

namespace LabsRV_Discussion.Services
{
    public class CommentService
    {
        private readonly MongoCommentRepository _repository;
        private readonly IMapper _mapper;

        public CommentService(MongoCommentRepository repository, IMapper mapper)
        {
            _repository = repository;
            _mapper = mapper;
        }

        public async Task<CommentResponseDto> CreateAsync(CommentRequestDto request)
        {
            if (request.ArticleId <= 0)
                throw new ArgumentException("A valid ArticleId is required.");

            if (string.IsNullOrWhiteSpace(request.Content) || request.Content.Length < 2 || request.Content.Length > 2048)
                throw new ArgumentException("Content must be between 2 and 2048 characters.");

            var comment = _mapper.Map<Comment>(request);
            var created = await _repository.AddAsync(comment);
            return _mapper.Map<CommentResponseDto>(created);
        }

        public async Task<CommentResponseDto> GetByIdAsync(int id)
        {
            var comment = await _repository.GetByIdAsync(id);
            if (comment == null)
                throw new ArgumentException("Comment not found");

            return _mapper.Map<CommentResponseDto>(comment);
        }

        public async Task<IEnumerable<CommentResponseDto>> GetAllAsync()
        {
            var comments = await _repository.GetAllAsync();

            return _mapper.Map<IEnumerable<CommentResponseDto>>(comments);
        }

        public async Task DeleteAsync(int id)
        {
            await _repository.DeleteAsync(id);
        }

        public async Task<CommentResponseDto> UpdateAsync(int id, CommentRequestDto request)
        {
            if (request.ArticleId <= 0)
                throw new ArgumentException("A valid ArticleId is required.");

            if (string.IsNullOrWhiteSpace(request.Content) || request.Content.Length < 2 || request.Content.Length > 2048)
                throw new ArgumentException("Content must be between 2 and 2048 characters.");

            var comment = await _repository.GetByIdAsync(id);
            comment.Content = request.Content;
            var updated = await _repository.UpdateAsync(comment);
            return _mapper.Map<CommentResponseDto>(updated);
        }
    }
}
