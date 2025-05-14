using AutoMapper;
using FluentValidation;
using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;
using Publisher.Exceptions;
using Publisher.Infrastructure.Validators;
using Publisher.Models;
using Publisher.Repositories.Interfaces;
using Publisher.Services.Interfaces;

namespace Publisher.Services.Implementations;

public class LabelService : ILabelService
{
    private readonly ILabelRepository _markRepository;
    private readonly IMapper _mapper;
    private readonly MarkRequestDTOValidator _validator;
    
    public LabelService(ILabelRepository markRepository, 
        IMapper mapper, MarkRequestDTOValidator validator)
    {
        _markRepository = markRepository;
        _mapper = mapper;
        _validator = validator;
    }
    
    public async Task<IEnumerable<LabelResponseDTO>> GetMarksAsync()
    {
        var marks = await _markRepository.GetAllAsync();
        return _mapper.Map<IEnumerable<LabelResponseDTO>>(marks);
    }

    public async Task<LabelResponseDTO> GetMarkByIdAsync(long id)
    {
        var mark = await _markRepository.GetByIdAsync(id)
                   ?? throw new NotFoundException(ErrorCodes.MarkNotFound, ErrorMessages.MarkNotFoundMessage(id));
        return _mapper.Map<LabelResponseDTO>(mark);
    }

    public async Task<LabelResponseDTO> CreateMarkAsync(LabelRequestDTO mark)
    {
        await _validator.ValidateAndThrowAsync(mark);
        var markToCreate = _mapper.Map<Label>(mark);
        var createdMark = await _markRepository.CreateAsync(markToCreate);
        return _mapper.Map<LabelResponseDTO>(createdMark);
    }

    public async Task<LabelResponseDTO> UpdateMarkAsync(LabelRequestDTO mark)
    {
        await _validator.ValidateAndThrowAsync(mark);
        var markToUpdate = _mapper.Map<Label>(mark);
        var updatedMark = await _markRepository.UpdateAsync(markToUpdate)
                          ?? throw new NotFoundException(ErrorCodes.MarkNotFound, ErrorMessages.MarkNotFoundMessage(mark.Id));
        return _mapper.Map<LabelResponseDTO>(updatedMark);
    }

    public async Task DeleteMarkAsync(long id)
    {
        if (!await _markRepository.DeleteAsync(id))
        {
            throw new NotFoundException(ErrorCodes.MarkNotFound, ErrorMessages.MarkNotFoundMessage(id));
        }
    }
}