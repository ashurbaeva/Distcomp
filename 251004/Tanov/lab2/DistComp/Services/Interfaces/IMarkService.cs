using DistComp.DTO.RequestDTO;
using DistComp.DTO.ResponseDTO;

namespace DistComp.Services.Interfaces;

public interface IMarkService
{
    Task<IEnumerable<LabelResponseDTO>> GetMarksAsync();

    Task<LabelResponseDTO> GetMarkByIdAsync(long id);

    Task<LabelResponseDTO> CreateMarkAsync(LabelRequestDTO mark);

    Task<LabelResponseDTO> UpdateMarkAsync(LabelRequestDTO mark);

    Task DeleteMarkAsync(long id);
}