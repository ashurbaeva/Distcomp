using Discussion.DTO.RequestDTO;
using Discussion.DTO.ResponseDTO;

namespace Discussion.Services.Interfaces;

public interface INoteService
{
    Task<IEnumerable<LabelResponseDTO>> GetMessagesAsync();

    Task<LabelResponseDTO> GetNoteByIdAsync(long id);

    Task<LabelResponseDTO> CreateNoteAsync(NoteRequestDTO note);

    Task<LabelResponseDTO> UpdateNoteAsync(NoteRequestDTO note);

    Task DeleteNoteAsync(long id);
}