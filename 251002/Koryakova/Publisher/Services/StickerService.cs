using AutoMapper;
using Microsoft.EntityFrameworkCore;
using Publisher.Data;
using Publisher.Models.DTOs.Requests;
using Publisher.Models.DTOs.Responses;
using Publisher.Models.Entities;
using Shared.Models.Queries;
using Publisher.Interfaces;
using Publisher.Storage;

namespace Publisher.Services
{
    public class StickerService
    {
        private readonly AppDbContext _appDbContext;
        private readonly ICrudRepository<Sticker> _stickerRepository;
        private readonly IMapper _mapper;

        public StickerService(IMapper mapper, ICrudRepository<Sticker> stickerRepository, AppDbContext appDbContext)
        {
            _stickerRepository = stickerRepository;
            _mapper = mapper;
            _appDbContext = appDbContext;
        }

        public StickerResponseTo CreateSticker(StickerRequestTo stickerRequestTo)
        {
            if (stickerRequestTo == null)
            {
                throw new ArgumentNullException(nameof(stickerRequestTo), "Sticker request cannot be null");
            }

            try
            {
                var sticker = _mapper.Map<Sticker>(stickerRequestTo);
                var createdSticker = _stickerRepository.Add(sticker);
                return _mapper.Map<StickerResponseTo>(createdSticker);
            }
            catch (DbUpdateException dbEx)
            {
                throw new InvalidOperationException(
                    dbEx.InnerException?.Message ?? "Database error occurred while creating news",
                    dbEx);
            }
            catch (AutoMapperMappingException mapEx)
            {
                throw new InvalidOperationException("Mapping failed", mapEx);
            }
            catch (Exception ex) when (ex is ArgumentException or
                              ArgumentNullException or
                              InvalidOperationException)
            {
                throw;
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException("Failed to create sticker", ex);
            }
        }

        public StickerResponseTo GetStickerById(long id)
        {
            if (id <= 0)
                throw new ArgumentException("ID must be positive", nameof(id));

            try
            {
                var sticker = _stickerRepository.GetById(id);
                return _mapper.Map<StickerResponseTo>(sticker);
            }
            catch(ArgumentException)
            {
                throw;
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException($"Failed to get sticker with ID {id}", ex);
            }
        }

        public IEnumerable<StickerResponseTo> GetAllStickers(QueryOptions<Sticker>? options = null)
        {
            try
            {
                var stickers = options != null ? _stickerRepository.GetFiltered(options) : _stickerRepository.GetAll();
                return _mapper.Map<IEnumerable<StickerResponseTo>> (stickers);
            }
            catch(Exception ex)
            {
                throw new InvalidOperationException("Failed to get all stickers", ex);
            }
        }

        public StickerResponseTo UpdateSticker(StickerRequestTo stickerRequestTo)
        {
            if (stickerRequestTo == null)
            {
                throw new ArgumentNullException(nameof(stickerRequestTo), "Sticker cannot be null");
            }
            if (stickerRequestTo.Id == null || stickerRequestTo.Id <= 0)
            {
                throw new ArgumentException("Invalid sticker ID", nameof(stickerRequestTo.Id));
            }

            try
            {
                var existingSticker = _stickerRepository.GetById(stickerRequestTo.Id.Value);
                if (existingSticker == null)
                {
                    throw new InvalidOperationException($"Editor {stickerRequestTo.Id} not found");
                }

                _mapper.Map(stickerRequestTo, existingSticker);
                var updatedSticker = _stickerRepository.Update(existingSticker);
                return _mapper.Map<StickerResponseTo>(updatedSticker);
            }
            catch (DbUpdateException ex)
            {
                throw new InvalidOperationException(ex.InnerException?.Message ?? "Database operation failed");
            }
            catch (Exception ex) when (ex is ArgumentException or
                              ArgumentNullException or
                              InvalidOperationException)
            {
                throw;
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException($"Failed to update sticker with ID {stickerRequestTo.Id}", ex);
            }
        }

        public bool DeleteSticker(long id)
        {
            if (id <= 0)
            {
                throw new ArgumentException("ID must be positive", nameof(id));
            }
            try
            {
                return _stickerRepository.DeleteById(id);
            }
            catch (DbUpdateException ex)
            {
                throw new InvalidOperationException(ex.InnerException?.Message ?? "Database operation failed");
            }
            catch (ArgumentException)
            {
                throw;
            }
            catch(Exception ex)
            {
                throw new InvalidOperationException($"Failed to delete a sticker with ID {id}", ex);
            }
        }

        public IEnumerable<StickerResponseTo> GetStickersByNewsId(long newsId)
        {
            if (newsId <= 0)
                throw new ArgumentException("Invalid News ID");

            try
            {
                // Join through NewsSticker table
                var stickerIds = _appDbContext.NewsStickers
                    .Where(ns => ns.NewsId == newsId)
                    .Select(ns => ns.StickerId)
                    .ToList();

                return _mapper.Map<IEnumerable<StickerResponseTo>>(
                    _appDbContext.Stickers
                        .Where(s => stickerIds.Contains(s.Id))
                );
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException("Search failed", ex);
            }
        }
    }
}
