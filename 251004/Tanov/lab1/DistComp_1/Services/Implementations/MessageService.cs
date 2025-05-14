using AutoMapper;
using DistComp_1.DTO.RequestDTO;
using DistComp_1.DTO.ResponseDTO;
using DistComp_1.Exceptions;
using DistComp_1.Infrastructure.Validators;
using DistComp_1.Models;
using DistComp_1.Repositories.Interfaces;
using DistComp_1.Services.Interfaces;
using FluentValidation;

namespace DistComp_1.Services.Implementations;

public class MessageService : IMessageService
{
    private readonly INoteRepository _messageRepository;
    private readonly IMapper _mapper;
    private readonly NoteRequestDTOValidator _validator;
    
    public MessageService(INoteRepository messageRepository, 
        IMapper mapper, NoteRequestDTOValidator validator)
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
        if (await _messageRepository.DeleteAsync(id) is null)
        {
            throw new NotFoundException(ErrorCodes.MessageNotFound, ErrorMessages.MessageNotFoundMessage(id));
        }
    }
}
