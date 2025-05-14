using AutoMapper;
using Discussion.DTO.RequestDTO;
using Discussion.DTO.ResponseDTO;
using Discussion.Exceptions;
using Discussion.Infrastructure.Validators;
using Discussion.Models;
using Discussion.Repositories.Interfaces;
using Discussion.Services.Interfaces;
using FluentValidation;

namespace Discussion.Services.Implementations;

public class NoteService : INoteService
{
    private readonly INoteRepository _messageRepository;
    private readonly IMapper _mapper;
    private readonly NoteRequestDtoValidator _validator;
    
    public NoteService(INoteRepository messageRepository, 
        IMapper mapper, NoteRequestDtoValidator validator)
    {
        _messageRepository = messageRepository;
        _mapper = mapper;
        _validator = validator;
    }
    
    public async Task<IEnumerable<NoteResponseDTO>> GetMessagesAsync()
    {
        var messages = await _messageRepository.GetAllAsync();
        return _mapper.Map<IEnumerable<NoteResponseDTO>>(messages);
    }

    public async Task<NoteResponseDTO> GetMessageByIdAsync(long id)
    {
        var message = await _messageRepository.GetByIdAsync(id)
                      ?? throw new NotFoundException(ErrorCodes.MessageNotFound, ErrorMessages.MessageNotFoundMessage(id));
        return _mapper.Map<NoteResponseDTO>(message);
    }

    public async Task<NoteResponseDTO> CreateMessageAsync(NoteRequestDTO message)
    {
        await _validator.ValidateAndThrowAsync(message);
        var messageToCreate = _mapper.Map<Note>(message);
        var createdMessage = await _messageRepository.CreateAsync(messageToCreate);
        return _mapper.Map<NoteResponseDTO>(createdMessage);
    }

    public async Task<NoteResponseDTO> UpdateMessageAsync(NoteRequestDTO message)
    {
        await _validator.ValidateAndThrowAsync(message);
        var messageToUpdate = _mapper.Map<Note>(message);
        var updatedMessage = await _messageRepository.UpdateAsync(messageToUpdate)
                             ?? throw new NotFoundException(ErrorCodes.MessageNotFound, ErrorMessages.MessageNotFoundMessage(message.Id));
        return _mapper.Map<NoteResponseDTO>(updatedMessage);
    }

    public async Task DeleteMessageAsync(long id)
    {
        if (!await _messageRepository.DeleteAsync(id))
        {
            throw new NotFoundException(ErrorCodes.MessageNotFound, ErrorMessages.MessageNotFoundMessage(id));
        }
    }
}
