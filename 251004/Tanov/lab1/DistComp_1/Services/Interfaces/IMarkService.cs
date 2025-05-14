using DistComp_1.DTO.RequestDTO;
using DistComp_1.DTO.ResponseDTO;

namespace DistComp_1.Services.Interfaces;

public interface IMarkService
{
    Task<IEnumerable<LabelResponseDTO>> GetMarksAsync();

    Task<LabelResponseDTO> GetMarkByIdAsync(long id);

    Task<LabelResponseDTO> CreateMarkAsync(MarkRequestDTO mark);

    Task<LabelResponseDTO> UpdateMarkAsync(MarkRequestDTO mark);

    Task DeleteMarkAsync(long id);
}