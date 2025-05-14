using AutoMapper;
using LabsRV_Articles.Models.Domain;
using LabsRV_Articles.Models.DTO;
using LabsRV_Articles.Models.Exceptions;
using LabsRV_Articles.MyApp.Models.Domain;
using LabsRV_Articles.Repositories;

namespace LabsRV_Articles.Services
{
    public class ArticleService : Service<Article, ArticleRequestDto, ArticleResponseDto>
    {
        protected new readonly ArticleRepository _repository;
        private readonly StickerRepository _stickerRepository;

        public ArticleService(
            ArticleRepository articleRepository,
            IMapper mapper,
            StickerRepository stickerRepository)
            : base(articleRepository, mapper)
        {
            _repository = articleRepository;
            _stickerRepository = stickerRepository;
        }

        /*public ArticleService(ArticleRepository repository, IMapper mapper)
            : base(repository, mapper)
        {
            this._repository = repository;
        }*/

        public override void Validate(ArticleRequestDto request)
        {
            if (request.AuthorId <= 0)
                throw new ArgumentException("A valid AuthorId is required.");
            if (string.IsNullOrWhiteSpace(request.Title) || request.Title.Length < 2 || request.Title.Length > 64)
                throw new ArgumentException("Title must be between 2 and 64 characters.");
            if (string.IsNullOrWhiteSpace(request.Content) || request.Content.Length < 4 || request.Content.Length > 2048)
                throw new ArgumentException("Content must be between 4 and 2048 characters.");
        }

        /*public override ArticleResponseDto Create(ArticleRequestDto request)
        {
            Validate(request);
            var article = _mapper.Map<Article>(request);
            article.created = DateTime.UtcNow;
            article.modified = article.created;
            // Обработка связи многие-ко-многим: если передан список StickerIds
            if (request.StickerIds != null && request.StickerIds.Any())
            {
                article.articleStickers = request.StickerIds
                    .Select(id => new ArticleSticker { stickerId = id })
                    .ToList();
            }

            var created = _repository.Add(article);
            return _mapper.Map<ArticleResponseDto>(created);
        }*/

        public async Task<ArticleResponseDto> DeleteAsync(int id)
        {
            var article = await _repository.GetByIdAsync(id);
            if (article == null)
                throw new ArgumentException($"Статья с id {id} не найдена");

            // ШАГ 1: Получаем все стикеры, связанные с этой статьёй
            var stickersToDelete = await _repository.GetStickersByArticleIdAsync(id);

            // ШАГ 2: Удаляем статью
            await _repository.DeleteAsync(id);

            // ШАГ 3: Проверяем и удаляем стикеры, если они больше не используются
            foreach (var sticker in stickersToDelete)
            {
                var isUsed = await _stickerRepository.IsUsedByOtherArticlesAsync(sticker.id, id);
                if (!isUsed)
                {
                    await _stickerRepository.DeleteAsync(sticker.id);
                }
            }

            return _mapper.Map<ArticleResponseDto>(article);
        }

        public async Task<ArticleResponseDto> CreateAsync(ArticleRequestDto request)
        {
            Validate(request);

            // Проверка уникальности заголовка
            bool exists = await _repository.UniqueTitleExistsAsync(request.Title);
            if (exists)
            {
                throw new AlreadyExistsException($"Заголовок '{request.Title}' уже существует.");
            }

            // ШАГ 1: Создаём статью
            var article = _mapper.Map<Article>(request);
            article.created = DateTime.UtcNow;
            article.modified = article.created;

            var createdArticle = await _repository.AddAsync(article);

            // ШАГ 2: Обрабатываем стикеры по именам
            if (request.StickerNames != null && request.StickerNames.Any())
            {
                // Находим существующие стикеры
                var existingStickers = await _stickerRepository.GetByNamesAsync(request.StickerNames);

                // Создаём новые, если их нет
                var newStickers = request.StickerNames
                    .Except(existingStickers.Select(s => s.name))
                    .Select(name => new Sticker { name = name })
                    .ToList();

                if (newStickers.Count > 0)
                {
                    await _stickerRepository.AddRangeAsync(newStickers);
                }

                // Объединяем стикеры
                var allStickerIds = existingStickers
                    .Concat(newStickers)
                    .Select(s => s.id)
                    .ToList();

                // ШАГ 3: Добавляем связи через репозиторий
                await _repository.AddStickersToArticleAsync(createdArticle.id, allStickerIds);
            }

            return _mapper.Map<ArticleResponseDto>(createdArticle);
        }
    }
}
