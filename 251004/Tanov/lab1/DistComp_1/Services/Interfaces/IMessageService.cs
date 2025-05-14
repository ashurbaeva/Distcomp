using DistComp_1.DTO.RequestDTO;
using DistComp_1.DTO.ResponseDTO;

namespace DistComp_1.Services.Interfaces;

public interface IMessageService
{
    Task<IEnumerable<NoteResponseDTO>> GetMessagesAsync();

    Task<NoteResponseDTO> GetMessageByIdAsync(long id);

    Task<NoteResponseDTO> CreateMessageAsync(NoteRequestDTO message);

    Task<NoteResponseDTO> UpdateMessageAsync(NoteRequestDTO message);

    Task DeleteMessageAsync(long id);
}