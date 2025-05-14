using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;

namespace Publisher.Services.Interfaces;

public interface ILabelService
{
    Task<IEnumerable<LabelResponseDTO>> GetMarksAsync();

    Task<LabelResponseDTO> GetMarkByIdAsync(long id);

    Task<LabelResponseDTO> CreateMarkAsync(LabelRequestDTO mark);

    Task<LabelResponseDTO> UpdateMarkAsync(LabelRequestDTO mark);

    Task DeleteMarkAsync(long id);
}